# Module 3-Chat
(Partially complete... work in progress)

This module portrays creating a simple chatting application using Hasura.

For building this ChatApp, we will be using Hasura Auth, Hasura Data and a Socket Server for sending messages which will be hosted as a CustomService on Hasura

## 1. Using Hasura Data Service

Go to your project console and head to **Data and Schema Management** present in the left side panel.

### Creating Tables

We will need to create two tables, one for storing user details and the other for storing chat messages.

#### UserDetails table:

Create a new table `user_details` and insert the following columns:

![Alt text](https://github.com/hasura/Modules-Android/blob/master/Module_3-Chat/add_new_table_chat_profile.png)

Click on create.

#### ChatMessage table:

Create a new table `chat_meassage'and insert the following columns:

![Alt text](https://github.com/hasura/Modules-Android/blob/master/Module_3-Chat/add_new_table_chat_message.png)

Click on create.

### Adding Permissions

By default, the Hasura Data Service allows only admin to access the table information. To open up access of information to role: user, we add permissions.

Head to **Modify Table, Relationships and Permissions** in the console and scroll to the bottom to find the permissions field.

#### UserDetails table:

Under Permissions, click on **Add Permissions for new Role** to add a new permission as follows.

![Alt text](https://github.com/hasura/Modules-Android/blob/master/Module_3-Chat/permissions_user_details.png)

``Check : {"user_id":"REQ_USER_ID"}``

The above statement says that allow access only if value of ``user_id`` equals ``REQ_USER_ID`` where "REQ_USER_ID" is a special varialbe used by the Hasura Data Service that holds the Hasura-User-Id of the currently logged in user.

#### ChatMessage table:

Under Permissions, click on **Add Permissions for new Role** to add a new permission as follows.

![Alt text](https://github.com/hasura/Modules-Android/blob/master/Module_3-Chat/permissions_chat_message.png)

## 2: Configuring the Hasura Android SDK:

Once you have created your android project, you will have to add Hasura-Android SDK and a few other dependencies. To do so, I am using Gradle.

Add the following code to the App level build.gradle file
```
    compile 'com.android.support:appcompat-v7:25.3.1'
    compile 'com.squareup.retrofit2:retrofit:2.1.0'
    compile 'com.squareup.retrofit2:converter-gson:2.1.0'
    compile 'com.squareup.okhttp3:logging-interceptor:3.4.1'
    compile 'com.android.support:recyclerview-v7:25.3.1'
    compile 'com.github.hasura.android-sdk:sdk:v0.0.5'
    compile 'com.android.support:cardview-v7:25.3.1'
    compile 'com.android.support:design:25.3.1'
    compile 'me.zhanghai.android.materialprogressbar:library:1.3.0'
    testCompile 'junit:junit:4.12'
    compile ('io.socket:socket.io-client:0.8.3') {
        exclude group: 'org.json', module: 'json'
    }
    annotationProcessor 'com.jakewharton:butterknife-compiler:8.5.1'
    compile 'io.github.luizgrp.sectionedrecyclerviewadapter:sectionedrecyclerviewadapter:1.0.4'
    compile 'com.github.jaisontj:BottomSheetImagePicker:v1.0'
    compile 'com.jakewharton:butterknife:8.5.1'
    compile 'com.android.support.constraint:constraint-layout:1.0.1'

```

Make the following changes to Project level build.gradle file
```
    allprojects {
        repositories {
          jcenter()

          maven { url 'https://jitpack.io' }
        }
    }

```

For more information go to [Hasura Android SDK](https://github.com/hasura/android-sdk).

## 3: Initializing your Hasura Project:

To access your Hasura Project through android, you will have to first initialize it.

This initialization should be before you start using the SDK(like beginning of your launcher activity), else you will get an error.
```
  Hasura.setProjectConfig(new ProjectConfig.Builder()
                .setProjectName("Project-Name")
                .build())
                .enableLogs()
                .initialise(this);

```
## 4: SignUp and Login:

Regarding the signUp/Login part, please refer to [Hasura Android Module 1-Login](https://github.com/hasura/Modules-Android/tree/master/Module_1-Login).

You can implement any one of the methods in the above module for performing SignUp/Login.

## 5: Storing Profile Information:

Regarding storing user information, please refer to [Hasura Android Module 2-Registration](https://github.com/hasura/Modules-Android/tree/master/Module_2-Registration).

In this case, we will also be storing the mobile number of the user in the user details table.

After the profile activity, direct the user to MainActivity, which will show the conversations of the user.

## 6: Socket Server and CustomService:

We build a socket-server using Socket.io for sending messages from one device to another.

### Step 1:

Clone the [Hasura Quick-Start](https://github.com/hasura/quickstart-docker-git) repository.

### Step 2: 

Copy the node-express folder. This will be your base folder.

Go to the folder app/src. Here you will find a package.json file, where you will have to add the dependencies for your node server.
///Add all npm commands

### Step 3:

Now we write our server.js file
```
// Initializing
var Express = require('express');
var http = require('http');
var _io = require('socket.io');
var rp = require('request-promise');

const app = new Express();
const server = new http.Server(app);
const io = _io(server);

/*
  The server will be adding the received messages to our database.
  Initializing contents for data query.
*/
const data_url = 'http://data.hasura/';
const headers = {
  'Content-Type': 'application/json',
	'X-Hasura-Role': 'admin',
	'X-Hasura-User-Id': 1
};

//When user connects to server
const sockets = {};
io.on('connection', (socket) => {
  console.log('User connected: ' + socket.id);
  if (socket.handshake.headers['x-hasura-user-role'] ===
    'anonymous') {
			console.log('User connected: anonymous');
    return;
  }
  
  //The socket Id of every user is identifies by his hasura-user-id(unique)
  
  const userId = socket.handshake.headers['x-hasura-user-id'];
  sockets[userId] = socket;
  console.log('Socket handshake accepted from: ' + userId.toString());

  //When server receives a message

  socket.on('chatMessage', (_params) => {
    try {
      const params = JSON.parse(_params);
			const sender_id = parseInt(userId, 10);
			const receiver_id = params.receiver_id;
			const message = params.content;
			const chattimestamp = params.time;
 
      //Adding to database via server
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

      //Sending message to the receiver
			rp(httpRequestOptions)
			    .then(function (parsedBody) {
						if (sockets[receiver_id]) {  //Finding the receiver based on his hasura-user-id
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

  //If user disconnects from server
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

```
### Step 4:
Now go to your Console and add a new CustomService by clicking the ``+`` button in the left side panel.

Give a name to your CustomService, enable Git Push under Image Details and then click `Create`.

### Step 5:
We have to add the Hasura remote now. From the terminal go to the folder that you had copied when you cloned Hasura Quick-Start(this-folder/app/src contains your server.js file)

Copy the link corresponding to `Add the Hasura remote` and enter it in the terminal.

Now enter `git push hasura master` to deploy your CustomService.
//Add image

## 6. Using the Socket-Server

### Basic usages

#### `socket.connect();`

This will connect to the socket server.

#### `socket.emit("chat message",msg)`

This wil emit "msg" to the chat server. "chat message" is like an identifier.

#### `socket.on("sent message",msg)`

This is used to receive a message which has an identifier "sent message".

For more visit [Socket.io](https://socket.io/get-started/chat/)

## 7. 










