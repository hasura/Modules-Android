# Module 2-Registration

This module portrays how to create a simple profile page.

This includes creating a new table under Hasura Data and performing basic CRUD operations.

## 1: Configuring the Hasura Android SDK:

Once you have created your android project, you will have to add [Hasura-Android SDK](https://github.com/hasura/android-sdk). 

## 2: Initializing your Hasura Project:

To access your Hasura Project through android, you will have to first initialize it.

```
  Hasura.setProjectConfig(new ProjectConfig.Builder()
                .setProjectName("Project-Name")
                .build())
                .enableLogs()
                .initialise(this);

```
## 3: SignUp and Login:

Regarding the signUp/Login part, please refer to [Hasura Android Module 1-Login](https://github.com/hasura/Modules-Android/tree/master/Module%201-Login).

You can implement any one of the methods in the above module for performing SignUp/Login.

## 4. Using Android SDK dataService():

  Here, we are going to use the Android SDK dataService to make calls to the Hasura Data Service.
  
### Step 1:
  Create a new HasuraUser object(say "user") and initialize it.
  ```
  HasuraUser user = Hasura.getClient.getUser();
  
  ```
### Step 2:
  Create a new HasuraClient object(say "user") and initialize it.
  ```
  HasuraClient client = Hasura.getClient();
  
  ```
### Step 3:
  Following is a small code snippet showing how to use the dataService. This is a sample code in which my RequestBody is of type SelectQuery and I expect my response of type UserDetails.class 
  ```
  client.useDataService()
                    .setRequestBody(new SelectQuery(user.getId()))
                    .expectResponseTypeOf(UserDetails.class)
                    .enqueue(new Callback<UserDetails, HasuraException>() {
                        @Override
                        public void onSuccess(UserDetails userDetails) {
                            
                        }

                        @Override
                        public void onFailure(HasuraException e) {
                        
                        }
                    });
  
  ```
  Here I am expecting a single response, so the above response type.
  
  If you are expecting an array type response, you can modify the particular line to 
  ```
  .expectResponseTypeArrayOf(UserDetails.class)
  
  ```
  Note that doing this would also modify the arguments of .enqueue() to adapt to array response.
  
## 5. Data Modelling:
  The SelectQuery above, is the select query for selecting data from the database.
  
  SelectQuery.class would could contain how my select query is modelled.
  
  Similarly InsertQuery.class and UpdateQuery,class model insert and update queries respectively.
  
  For more about modelling, refer the [Hasura Data Docs](https://hasura.io/_docs/platform/0.6/ref/data/reference.html)
  
  Example:
  ```
  {  "type" : "insert",
         "args" : {
           "table"     : "user_details",
           "objects"   : [
             {
               "name"   : "Name",
               "status" : "Status"
             }
             
           ]  
         }
      }
  
  ```
  For such a query, the data model would be like:
  ```
  @SerializedName("type")
    String type = "insert";

    @SerializedName("args")
    Args args;

    class Args{
        @SerializedName("table")
        String table = "user_details";

        @SerializedName("objects")
        Object objects;
    }
    
    class Object{
         @SerializedName("name)
         String name;
         
         @SerializedName("status")
         String status;
    }
  
  ```
  Writing a constructor for initializing InsertQuery:
  ```
  public InsertQuery(UserDetails userDetails){
        args = new Args();
        args.objects = new Object();
        args.objects.name = userDetails.getName();
        args.objects.status = userDetails.getStatus();
    }
  
  ```

## 6. The Flow of Work:

### Step 1:
Once you are done with SignUp/Login, you have to first check if the user is already logged in or not(The reason for this step is that once a login happens your auth token is stored locally so that you don't have to login every time you open the app.)

### Step 2:
Now, we make a select query to see whether the user has previously updated his profile. If yes, that is loaded into the respective fields, and update(used as a flag) is set to 1. 

### Step 3: Updating Profile.
When the user click the imageView, the camera opens up where the user can update the profile.
On filling the required fields, user can update his profile by clicking the update button.

### Step 4: Inserting/Updating.
When the button is clicked, create new object(here userDetails) and add the respective quantities to it.
If the update flag is 1, we perform update(Through UpdateQuery request body) else we perform insert(Through InsertQuery request body).
