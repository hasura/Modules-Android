# Module 2-Registration

This module portrays how to create a simple profile page.

This includes creating a new table under Hasura Data Service and performing basic CRUD operations.

Once the user is authenticated, we would like to store some additional information about the user. in this case, I would be storing his name, status, fileId of his profile picture and his mobile number.

To do this, we will be using the Hasura Data Service.

## 1: Using Hasura Data Service:

### Creating a new table

Go to your project console and head to Data and Schema Management present in the left side panel.

Here, create a new table user_details.

Insert the following columns to the user_details table:

1. name ( as Text)

2. status ( as Text)

3. user_id ( as Int)

4. file_id (as Text)

user_id will be used to identify which user can access the above fields.

### Adding Permissions

To limit who can access this data, we have to set some permissions.

Head to Modify Table, Relationships and Permissions in the console and scroll to the bottom to find the permissions field

Under Permissions, click on ** Add Permissions for new Role ** to add a new permission as follows.

//Add image here.


## 2: Configuring the Hasura Android SDK:

Once you have created your android project, you will have to add Hasura-Android SDK and a few other dependencies. To do so, I am using Gradle.

Add the following code to the App level build.gradle file
```
    compile 'com.android.support:appcompat-v7:25.3.1'
    compile 'com.squareup.retrofit2:retrofit:2.1.0'
    compile 'com.squareup.retrofit2:converter-gson:2.1.0'
    compile 'com.squareup.okhttp3:logging-interceptor:3.4.1'
    compile 'com.android.support:recyclerview-v7:25.3.1'
    compile 'com.github.hasura.android-sdk:sdk:v0.0.4'
    compile 'com.android.support:design:25.3.1'
    testCompile 'junit:junit:4.12'

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

Regarding the signUp/Login part, please refer to [Hasura Android Module 1-Login](https://github.com/hasura/Modules-Android/tree/master/Module%201-Login).

You can implement any one of the methods in the above module for performing SignUp/Login.

## 5. Storing User Details:

### Step 1: Check if user has already filled profile details.

We will now make a query to the table we created earlier to check if the user already has filled his profile information, if yes, load that information.

To do this, we have to fire a select query.

#### Modelling the SelectQuery:
```
public class SelectQuery {
    @SerializedName("type")
    String type = "select";

    @SerializedName("args")
    Args args;

    class Args{
        @SerializedName("table")
        String table = "user_details";

        @SerializedName("columns")
        String[] columns = {"name","status","user_id","file_id"};

        @SerializedName("where")
        Where where;
    }

    class Where{
        @SerializedName("user_id")
        Integer userId;
    }

    public SelectQuery(Integer userId){
        args = new Args();
        args.where = new Where();
        args.where.userId = userId;
    }
}

```
For more about modelling, refer the [Hasura Data Docs](https://hasura.io/_docs/platform/0.6/ref/data/reference.html)

To make calls to Hasura, we use the HasuraClient instance provided by the SDK
```
HasuraClient client = Hasura.getClient();

```

#### Making the call:

If we get the information, we will use the Hasura FileStore to download the profile picture.

We extract File-Id from the response we get, and then download that particular file.
```
client.useDataService()
                    .setRequestBody(new SelectQuery(userId)
                    .expectResponseTypeArrayOf(UserDetails.class)
                    .enqueue(new Callback<List<UserDetails>, HasuraException>() {
                        @Override
                        public void onSuccess(List<UserDetails> userDetailsList) {
                            
                            client.useFileStoreService()
                                    .downloadFile("File-Id", new FileDownloadResponseListener() {
                                        @Override
                                        public void onDownloadComplete(byte[] bytes) {
                                            
                                        }

                                        @Override
                                        public void onDownloadFailed(HasuraException e) {
                                            
                                        }

                                        @Override
                                        public void onDownloading(float v) {

                                        }
                                    });
                        }

                        @Override
                        public void onFailure(HasuraException e) {
                        
                        }
                    });

```
#### Uploading Data

Depending on whether the user already has profile information stored or not, we will update/ insert into the table respectively.

##### Update Query:
```
public class UpdateQuery {
    @SerializedName("type")
    String type = "update";

    @SerializedName("args")
    Args args;

    class Args{
        @SerializedName("table")
        String table = "user_details";

        @SerializedName("$set")
        $Set set;

        @SerializedName("where")
        Where where;
    }

    class $Set{
        @SerializedName("name")
        String name;

        @SerializedName("status")
        String status;
    }

    class Where{
        @SerializedName("user_id")
        Integer userId;
    }

    public UpdateQuery(UserDetails userDetails){
        args = new Args();
        args.set = new $Set();
        args.set.name = userDetails.getName();
        args.set.status = userDetails.getStatus();
        args.where = new Where();
        args.where.userId = userDetails.getId();
    }
}


```

##### Insert Query:
```
public class InsertQuery {
    @SerializedName("type")
    String type = "insert";

    @SerializedName("args")
    Args args;

    class Args{
        @SerializedName("table")
        String table = "user_details";

        @SerializedName("objects")
        List<UserDetails> objects;
    }

    public InsertQuery(UserDetails userDetails){
        args = new Args();
        args.objects = new ArrayList<>();
        args.objects.add(userDetails);
    }
}

```
#### Making the call:

When the submit button is clicked, we should add name,status and other details to the userDetails object and upload to the database.
```
button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = ((BitmapDrawable)picture.getDrawable()).getBitmap();
                byte[] image = Picture.getBytes(bitmap);
                final UserDetails userDetails = new UserDetails();
                userDetails.setName(name.getText().toString().trim());
                userDetails.setStatus(status.getText().toString().trim());
                userDetails.setId(Hasura.getClient().getUser().getId());

                client.useFileStoreService()
                        .uploadFile(image, "image/*", new FileUploadResponseListener() {
                                 @Override
                                 public void onUploadComplete(FileUploadResponse fileUploadResponse) {
                                     userDetails.setFileId(fileUploadResponse.getFile_id());
                                         client.useDataService()
                                                 .setRequestBody(new "InsertQuery/UpdateQuery"(userDetails))
                                                 .expectResponseType(ResponseMessage.class)
                                                 .enqueue(new Callback<ResponseMessage, HasuraException>() {
                                                     @Override
                                                     public void onSuccess(ResponseMessage responseMessage) {
                                                     
                                                     }

                                                     @Override
                                                     public void onFailure(HasuraException e) {
                                                     
                                                     }
                                                 });
                                     } 
                                 }

                                 @Override
                                 public void onUploadFailed(HasuraException e) {
                                 
                                 }
                        });

            }
        });

```
