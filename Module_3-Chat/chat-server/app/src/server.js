var Express = require('express');
var http = require('http');
var _io = require('socket.io');
var rp = require('request-promise');

const app = new Express();
const server = new http.Server(app);
const io = _io(server);

const data_url = 'http://data.hasura/';
const headers = {
  'Content-Type': 'application/json',
	'X-Hasura-Role': 'admin',
	'X-Hasura-User-Id': 1
};

const sockets = {};
io.on('connection', (socket) => {
  console.log('User connected: ' + socket.id);
  if (socket.handshake.headers['x-hasura-user-role'] ===
    'anonymous') {
			console.log('User connected: anonymous');
    return;
  }

  const userId = socket.handshake.headers['x-hasura-user-id'];
  sockets[userId] = socket;
  console.log('Socket handshake accepted from: ' + userId.toString());


  socket.on('chatMessage', (_params) => {
    try {
      const params = JSON.parse(_params);
			const sender_id = parseInt(userId, 10);
			const receiver_id = params.receiver_id;
			const message = params.content;
			const chattimestamp = params.time;

			var httpRequestOptions = {
			    method: 'POST',
			    uri: data_url + 'v1/query',
					headers: headers,
			    body: {
						type: 'insert',
						args: {
							table: 'chat_message',
							objects: [{
								content: message,
								time: chattimestamp,
								sender_id: sender_id,
								receiver_id: receiver_id,
								user_id: sender_id
							}]
						}
					},
			    json: true // Automatically stringifies the body to JSON
			};

			rp(httpRequestOptions)
			    .then(function (parsedBody) {
						if (sockets[receiver_id]) {
							const toSocket = sockets[receiver_id];
							toSocket.emit('chatMessage', JSON.stringify({
								params
							}));
							console.log('Emmitted to connected user: ' + _params);
						} else {
							console.log('User not connected to socket');
						}
			    })
			    .catch(function (err) {
			        console.log('Error adding to db :' + err.toString());
			    });
    } catch (e) {
      console.error(e);
      console.error(e.stack);
      console.error(
        'Some error in the "chatMessage" event');
    }
  });

  socket.on('disconnect', () => {
    if (userId) {
      sockets[userId] = null;
      console.log('User: ' + userId + ' disconnected');
    }
  });
});

server.listen(8080, function() {
	console.log('Server app listening on port 8080!');
});
