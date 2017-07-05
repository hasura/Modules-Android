var express = require('express');
var app = express();
var http = require('http').Server(app);
var io = require('socket.io')(http);

//your routes here
app.get('/', function (req, res) {
    res.send("Hello World!");
});

io.on('connection',function(socket){
	console.log('user connected');
	socket.on('join',function(data){
		console.log('user_id'+data);
		socket.join(data);
	})
	socket.on('chatMessage',function(msg,receiverId){
		console.log('message :' + receiverId);
		/*var sockets = io.sockets.sockets;
		sockets.forEach(function(sock){
			if(sock.id != socket.id){
				sock.emit('chat message');
			}
		})*/
		JSON.stringify(msg);

		io.sockets.in(receiverId).emit('sendMessage',msg);
		console.log('emit working');
	})

	socket.on('disconnect',function(){
		console.log('user disconnected');
	})
});


app.listen(8080, function () {
  console.log('Example app listening on port 8080!');
});
