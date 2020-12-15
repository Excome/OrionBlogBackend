# Blog Education Project

This documentation describes the API available, which you can use when writing your blog.

This WEB application is built on the RESP API architecture. You can deploy a shared back-end server and access it for information from client devices - either a browser or a mobile application.

### Contents of API
1. [Authorization](#Authorization)
   - [Registration](#Registration)
   - [Login](#Login)
   - [Logout](#Logout)
1. [Users](#Users)
   - [Users list](#Users list)
   - [Users list search by username](#Users list search by username)
   - [User profile](#User profile)
   - [User posts](#User posts)
   - [Edit user profile](#Edit user profile)
   - [Edit user password](#Edit user password)
1. [Posts](#Posts)
    - [Posts list](#Posts list)
    - [Single post](#Single post)
    - [Posts list by topic/tag/author](#Posts list by topic/tag/author)
    - [Create post](#Create post)
    - [Edit post](#Edit post)
    - [Delete post](#Delete post)
1. [Comments](#Comments)
    - [Post comments](#Post comments)
    - [Add post comment](#Add post comment)
    - [Edit post comment](#Edit post comment)
    - [Delete post comment](#Delete post comment)
1. [Follows](#Follows)
    - [List of User followers](#List of User followers)
    - [List of User follows](#List of User follows)
    - [Follow to anouther user](#Follow to anouther user)
    - [Unfollow from anouther user](#Unfollow from anouther user)
1. [Administration](#Administration)
    - [Admin's list of users](#Admin's list of users)
    - [Admin's user profile](#Admin's user profile)
    - [Delete user](#Delete user)
    - [Admin's edit user profile](#Admin's edit user profile)
    - [Admin's edit user password](#Admin's edit user password)
    - [Admin's list of posts](#Admin's list of posts)
    - [Admin's delete post](#Admin's delete post)
1. [Error response's](#Error response's)
    - [Non-domain errors](#Not domain errors)
    - [User errors](#User errors)
    - [Registration and User editing errors](#Registration and User editing errors)
    - [Post errors](#Post errors)
    - [Comment errors](#Comment errors)
    - [Tag errors](#Tag errors)

   
   
## Authorization
### Registration

#### Description
User registration

#### Request example
##### Headers
```
POST /registration
Content-Type: application/json
```
##### Body
```json
{
  "username": "user",
  "email": "example@email.com",
  "password": "123456",
  "passwordConfirm": "123456"
}
```
#### Response example
##### Headers
```
Content-Type: application/json
HTTP Status: 200 OK
```
##### Body
```json
{
    "id": 51,
    "username": "user",
    "createdDate": "10.11.2020 10:42",
    "roles": [
        {
            "name": "ROLE_USER"
        }
    ]
}
```

### Login

#### Description
User access to the application.
Authorization is carried out while saving the user's session in the database.
In order for the user to be authorized, you need to send a request and receive a **Cookie** in response and **save** in the client's browser


#### Request example
##### Headers
```
POST /login
Content-Type: application/json
```
##### Body
```json
{
  "username": "user",
  "password": "123pass"
}
```
#### Successful response example
##### Headers
```
Content-Type: application/json
HTTP Status: 200 OK
Set-Cockie: SESSION
```
##### Body
```json
{
    "auth": "SUCCESS",
    "username": "user"
}
```

#### Unsuccessful response example
##### Headers
```
Content-Type: application/json
HTTP Status: 401 Unauthorized
```
##### Body
```json
{
    "auth": "DENIED"
}
```
### Logout

#### Description
The user can logout of the application. 
To do this, you need to go to this resource after which the server deleted the user session from this device.

#### Request example
##### Headers
```
GET /logout
Content-Type: application/json
```
##### Body
```
don't need it
```
#### Response example
##### Headers
```
Content-Type: application/json
HTTP Status: 200 OK
```
##### Body
```json
{
    "auth": "LOGOUT",
    "username": "user"
}
```

## Users
### Users list

#### Description
For this resource you can get a list of registered users in reverse chronological order. Ie the last registered users. 
This resource supports pagination. 
Pagination starts from index zero and by default (i.e. without parameters passed in the request) a list of 15 most recently registered users is returned.
For pagination, you need to add parameters to the request:
```
page: {N} 
size: {N}
(default page=0, size=15)
Example: GET /users?page=2&size=20
```
#### Request example
##### Headers
```
GET /users
Content-Type: application/json
```
##### Body
```
don't need it
```
#### Response example
##### Headers
```
Content-Type: application/json
HTTP Status: 200 OK
```
##### Body
```json
[
    {
        "id": 3,
        "username": "user1",
        "createdDate": null,
        "roles": [
            {
                "name": "ROLE_USER"
            },
            {
                "name": "ROLE_ADMIN"
            }
        ]
    },

    

    {
        "id": 51,
        "username": "user5",
        "createdDate": "10.11.2020 10:42",
        "roles": [
            {
                "name": "ROLE_USER"
            }
        ]
    }    
]
```
### Users list search by username
```
GET /users?username={username}
```
#### Description
You can find users by match in the username.
This resource supports pagination. 
Pagination starts from index zero and by default (i.e. without parameters passed in the request) a list of 15 most recently registered users is returned.

For pagination, you need to add parameters to the request:
```
page: {N} 
size: {N}
(default page=0, size=15)
Example: GET /users?username=username&page=2&size=20
```
#### Request example
##### Headers
```
GET /users?username=user
Content-Type: application/json
```
##### Body
```
don't need it
```
##### Response example
##### Headers
```
Content-Type: application/json
HTTP Status: 200 OK
```
##### Body
```json
[
    {
        "id": 3,
        "username": "user1",
        "createdDate": null,
        "roles": [
            {
                "name": "ROLE_USER"
            },
            {
                "name": "ROLE_ADMIN"
            }
        ]
    },

    

    {
        "id": 51,
        "username": "user10",
        "createdDate": "10.11.2020 10:42",
        "roles": [
            {
                "name": "ROLE_USER"
            }
        ]
    }    
]
```

### User profile

#### Description
For this api you can get the profile data of an individual user
#### Request example
##### Headers
```
GET /users/someUserName
Content-Type: application/json
```
##### Body
```
don't need it
```
#### Response example
##### Headers
```
Content-Type: application/json
HTTP Status: 200 OK
```
##### Body
```json
{
    "id": 2,
    "email": "email@example.com",
    "username": "someUserName",
    "privateStatus": false,
    "surname": "surname",
    "name": "name",
    "birthday": "02.07.1999",
    "createdDate": "22.10.2020 10:02",
    "roles": [
        {
            "name": "ROLE_USER"
        }
    ]
}


```
### User posts
#### Description
For this resource, you can get shortcut posts of an individual user.
This resource supports pagination. 
Pagination starts from index zero and by default (i.e. without parameters passed in the request) a list of 15 most recently registered users is returned.
For pagination, you need to add parameters to the request:
```
page: {N} 
size: {N}
(default page=0, size=15)
Example: GET /users/alex22/posts?page=2&size=20
```
#### Request example
##### Headers
```
GET /users/someUserName
Content-Type: application/json
```
##### Body
```
don't need it
```
#### Response example
##### Headers
```
HTTP Status: 200 OK
Content-Type: application/json
```
##### Body
```json
[
    {
        "id": 5,
        "author": {
            "id": 3,
            "username": "someUserName"
        },
        "topic": "Very interesting post about something",
        "createdDate": "22.10.2020 11:15",
        "lastUpdatedDate": "22.10.2020 11:15"
    }
]
```

### Edit user profile
#### Description

An authorized user can change his profile data
(surname, name, birthday, privateStatus)

Field username must match the username of the authorized user
#### Request example
##### Headers
```
PUT /auth/settings/profile
Content-Type: application/json
Cookie: SESSION
```
##### Body
```json
{
    "username": "user",
    "surname":"newSurname",
    "name":"newName",
    "birthday":"10.10.1980",
    "privateStatus": true
}
```
#### Response example
##### Headers
```
HTTP Status: 200 OK
Content-Type: application/json
```
##### Body
```json
{
    "id": 3,
    "email": "email@example.com",
    "username": "user",
    "privateStatus": true,
    "surname": "newSurname",
    "name": "newName",
    "birthday": "10.10.1980",
    "createdDate": "12.10.2020 18:36",
    "roles": [
        {
            "name": "ROLE_ADMIN"
        },
        {
            "name": "ROLE_USER"
        }
    ]
}
```

### Edit user password
#### Description

An authorized user can change password.

Field username must match the username of the authorized user
#### Request example
##### Headers
```
PUT /auth/settings/security
Content-Type: application/json
Cookie: SESSION
```
##### Body
```json
{
    "username": "user",
    "password":"newPassword1234",
    "passwordConfirm":"newPassword1234"
}
```
#### Response example
##### Headers
```
HTTP Status: 200 OK
Content-Type: application/json
```
##### Body
```json
{
    "id": 3,
    "email": "email@example.com",
    "username": "user",
    "privateStatus": true,
    "surname": "newSurname",
    "name": "newName",
    "birthday": "10.10.1980",
    "createdDate": "12.10.2020 18:36",
    "roles": [
        {
            "name": "ROLE_ADMIN"
        },
        {
            "name": "ROLE_USER"
        }
    ]
}
```
## Posts
### Posts list

#### Description
For this resource you can get a list of the last created posts. 
This resource supports pagination. 
Pagination starts from index zero and by default (i.e. without parameters passed in the request) a list of 15 most recently created posts is returned.
For pagination, you need to add parameters to the request:
```
page: {N} 
size: {N}
(default page=0, size=15)
Example: GET /posts?page=2&size=20
```
#### Request example
##### Headers
```
GET /posts
Content-Type: application/json
```
##### Body
```
don't need it
```
#### Response example
##### Headers
```
Content-Type: application/json
HTTP Status: 200 OK
```
##### Body
```json
[
  {
        "id": 1234,
        "author": {
            "id": 3,
            "username": "user"
        },
        "topic": "Anouther test topic",
        "text": "something text",
        "tags": [
            {
                "name": "tag8"
            },
            {
                "name": "tag2"
            }
        ],
        "createdDate": "23.10.2020 07:28",
        "lastUpdatedDate": "23.10.2020 07:28"
    },


    {
        "id": 3435,
        "author": {
            "id": 306,
            "username": "user"
        },
        "topic": "Anouther test topic",
        "text": "something text",
        "tags": [
            {
                "name": "tag8"
            },
            {
                "name": "tag2"
            }
        ],
        "createdDate": "23.10.2020 07:28",
        "lastUpdatedDate": "23.10.2020 07:28"
    }
]
```

### Posts news feed

#### Description
For this resource, an authorized user can get a list of the latest posts of those users to which he is subscribed
This resource supports pagination. 
Pagination starts from index zero and by default (i.e. without parameters passed in the request) a list of 15 most recently created posts is returned.
For pagination, you need to add parameters to the request:
```
GET /posts/feed
```

```
page: {N} 
size: {N}
(default page=0, size=15)
Example: GET /posts/feed?page=2&size=20
```
#### Request example
##### Headers
```
GET /posts/feed
Content-Type: application/json
Cookie: SESSION
```
##### Body
```
don't need it
```
#### Response example
##### Headers
```
Content-Type: application/json
HTTP Status: 200 OK
```
##### Body
```json
[
  {
        "id": 1234,
        "author": {
            "id": 3,
            "username": "user3"
        },
        "topic": "Anouther test topic",
        "text": "something text",
        "tags": [
            {
                "name": "tag8"
            },
            {
                "name": "tag2"
            }
        ],
        "createdDate": "23.10.2020 07:28",
        "lastUpdatedDate": "23.10.2020 07:28"
    },


    {
        "id": 3435,
        "author": {
            "id": 306,
            "username": "user3"
        },
        "topic": "Anouther test topic",
        "text": "something text",
        "tags": [
            {
                "name": "tag8"
            },
            {
                "name": "tag2"
            }
        ],
        "createdDate": "23.10.2020 07:28",
        "lastUpdatedDate": "23.10.2020 07:28"
    }
]
```


### Single post

#### Description
For this resource, you can get a separate post
```
GET /posts/:postId
```
#### Request example
##### Headers
```
GET /posts/643
Content-Type: application/json
```
##### Body
```
don't need it
```
#### Response example
##### Headers
```
Content-Type: application/json
HTTP Status: 200 OK
```
##### Body
```json
{
    "id": 643,
    "author": {
        "id": 3257,
        "username": "someUser"
    },
    "topic": "...topic...",
    "text": "...text...",
    "tags": [
        {
            "name": "..tag.."
        }
    ],
    "createdDate": "23.10.2020 07:28",
    "lastUpdatedDate": "13.11.2020 10:32"
}
```
### Posts list by topic/tag/author

#### Description
For these resources, you can select a list of posts by search criteria:
1) by mutch in the post topic
2) by tag
3) by author
```
GET /posts/search/topic?topic={:topic}
GET /posts/search/tag?tag={:tag}
GET /posts/search/author?username={:username}
```
This resource supports pagination. 
Pagination starts from index zero and by default (i.e. without parameters passed in the request) a list of 15 most recently created posts is returned.
For pagination, you need to add parameters to the request:
```
page: {N} 
size: {N}
(default page=0, size=15)
Example: GET /posts/search/topic?topic=Vue.js&page=2&size=20
```
#### Request example
##### Headers
```
GET /posts/search/topic?topic=Vue.js
Content-Type: application/json
```
##### Body
```
don't need it
```
#### Response example
##### Headers
```
Content-Type: application/json
HTTP Status: 200 OK
```
##### Body
```json
[
  {
    "id": 643,
    "author": {
        "id": 3257,
        "username": "someUser"
    },
    "topic": "....Vue.js",
    "text": "...text...",
    "tags": [
        {
            "name": "..tag.."
        }
    ],
    "createdDate": "23.10.2020 07:28",
    "lastUpdatedDate": "13.11.2020 10:32"
  },

  {
    "id": 635,
    "author": {
        "id": 276,
        "username": "user"
    },
    "topic": "...Vue.js...",
    "text": "...text...",
    "tags": [
        {
            "name": "Vuex"
        },
        {
            "name": "Vue.js"
        }
    ],
    "createdDate": "18.112.2020 13:29",
    "lastUpdatedDate": "12.11.2020 13:29"
  }
]
```

### Create post

#### Description

An authorized user can write his post on this resource
```
POST /posts
```
#### Request example
##### Headers
```
POST /posts
Content-Type: application/json
Cookie: SESSION
```
##### Body
```json
{
    "topic":"...topic",
    "text":"...text...",
    "tags":["tag1", "tag2"]
}
```
#### Response example
##### Headers
```
HTTP Status: 200 OK
Content-Type: application/json
```
##### Body
```json
{
    "id": 45,
    "author": {
        "id": 33,
        "username": "user"
    },
    "topic": "...topic...",
    "text": "...text...",
    "tags": [
        {
            "name": "tag1"
        },
        {
            "name": "tag2"
        }
    ],
    "createdDate": "14.11.2020 12:57",
    "lastUpdatedDate": "14.11.2020 12:57"
}
```
### Edit post

#### Description


For this resource, an authorized user who is the author(or user with admin's role) of the post can change it.

```
PUT /posts/:postId
```
#### Request example
##### Headers
```
PUT /posts/6
Content-Type: application/json
Cookie: SESSION
```
##### Body
```json
{
    "topic":"new Topic",
    "text":"new very interesting Text",
    "tags":["new tag1", "new tag2"]
}
```
#### Response example
##### Headers
```
HTTP Status: 200 OK
Content-Type: application/json
```
##### Body
```json
{
    "id": 62,
    "author": {
        "id": 35,
        "username": "user"
    },
    "topic": "new Topic",
    "text": "new very interesting Text",
    "tags": [
        {
            "name": "new Tag2"
        },
        {
            "name": "new Tag1"
        }
    ],
    "createdDate": "23.10.2020 07:28",
    "lastUpdatedDate": "13.11.2020 10:32"
}
```

### Delete post

#### Description


For this resource, an authorized user who is the author(or user with admin's role) of the post can delete it.

```
DELETE /posts/:postId
```
#### Request example
##### Headers
```
DELETE /posts/45
Content-Type: application/json
Cookie: SESSION
```
##### Body
```
don't need it
```
#### Response example
##### Headers
```
HTTP Status: 200 OK
Content-Type: application/json
```
##### Body
```
don't need it
```

## Comments
### Post comments
#### Description
For this resource, you can get a list of comments for a specific post
```
GET /posts/:postId/comments
```
This resource supports pagination. 
Pagination starts from index zero and by default (i.e. without parameters passed in the request) a list of 15 most recently created comments is returned.
For pagination, you need to add parameters to the request:
```
page: {N} 
size: {N}
(default page=0, size=15)
Example: GET/posts/289/comments?&page=2&size=20
```
#### Request example
##### Headers
```
GET /posts/31/comments
Content-Type: application/json
```
##### Body
```
don't need it
```
#### Response example
##### Headers
```
HTTP Status: 200 OK
Content-Type: application/json
```
##### Body
```json
[
    {
        "id": 42,
        "author": {
            "id": 25,
            "username": "user1"
        },
        "text": "test comment 1",
        "createdDate": "13.11.2020 16:05",
        "lastUpdatedDate": "13.11.2020 16:05"
    },
    {
        "id": 40,
        "author": {
            "id": 25,
            "username": "user2"
        },
        "text": "comment 2",
        "createdDate": "13.11.2020 16:05",
        "lastUpdatedDate": "13.11.2020 16:05"
    },
    {
        "id": 36,
        "author": {
            "id": 25,
            "username": "user3"
        },
        "text": "comment 4",
        "createdDate": "13.11.2020 16:05",
        "lastUpdatedDate": "13.11.2020 16:05"
    }
]
```

### Add post comment
#### Description
For this resource, an authorized user can leave a comment under the post
```
POST /posts/:postId/comments
```
#### Request example
##### Headers
```
POST /posts/31/comments
Content-Type: application/json
Cookie: SESSION
```
##### Body
```json
{
    "author": {"username":"user"},
    "text":"This post so amazing!"
}
```
#### Response example
##### Headers
```
HTTP Status: 200 OK
Content-Type: application/json
```
##### Body
```json
{
    "id": 48,
    "author": {
        "id": 22,
        "username": "user"
    },
    "text": "This post so amazing!",
    "createdDate": "15.11.2020 11:15",
    "lastUpdatedDate": "15.11.2020 11:15"
}
```

### Edit post comment
#### Description
For this resource, an authorized user who is the author of the comment(or user has admin's role) can change the comment
```
PUT /posts/:postId/comments/:commentId
```
#### Request example
##### Headers
```
PUT /posts/31/comments/48
Content-Type: application/json
Cookie: SESSION
```
##### Body
```json
{
    "author": {"username": "user"},
    "text":"This post so amazing! (edit)"
}
```
#### Response example
##### Headers
```
HTTP Status: 200 OK
Content-Type: application/json
```
##### Body
```json
{
    "id": 48,
    "author": {
        "id": 3,
        "username": "admin"
    },
    "text": "This post so amazing! (edit)",
    "createdDate": "15.11.2020 11:15",
    "lastUpdatedDate": "15.11.2020 11:24"
}
```

### Delete post comment
#### Description
For this resource, an authorized user who is the author of the comment(or user has admin's role) can delete the comment
```
DELETE /posts/:postId/comments/:commentId
```
#### Request example
##### Headers
```
DELETE /posts/31/comments/48
Content-Type: application/json
Cookie: SESSION
```
##### Body
```
don't need it
```
#### Response example
##### Headers
```
HTTP Status: 200 OK
Content-Type: application/json
```
##### Body
```
no content
```

## Follows
### List of User followers
#### Description
For this resource you can get a list of user followers
```
GET /users/:username/subscription/followers
```
#### Request example
##### Headers
```
GET /users/someUser/subscription/followers
Content-Type: application/json
```
##### Body
```
none
```
#### Response example
##### Headers
```
HTTP Status: 200 OK
Content-Type: application/json
```
##### Body
```json
[
    {
        "id": 27,
        "username": "user1"
    },
    {
        "id": 25,
        "username": "user2"
    },
    {
        "id": 28,
        "username": "user3"
    }
]
```

### List of User follows
#### Description
For this resource you can get a list of user follow
```
GET /users/:username/subscription/follow
```
#### Request example
##### Headers
```
GET /users/someUser/subscription/follow
Content-Type: application/json
```
##### Body
```
none
```
#### Response example
##### Headers
```
HTTP Status: 200 OK
Content-Type: application/json
```
##### Body
```json
[
    {
        "id": 272,
        "username": "Cristiano"
    },
    {
        "id": 2537,
        "username": "Anonymus"
    }
]
```

### Follow to anouther user
#### Description
For this resource, an authorized user can follow to another user
```
POST /users/:username/follow
```
#### Request example
##### Headers
```
POST /users/someUser/follow
Content-Type: application/json
Cookie: SESSION
```
##### Body
```json
{
    "follower":{"username":"userOne"},
    "follow":{"username":"someUser"}
}
```
#### Response example
##### Headers
```
HTTP Status: 200 OK
Content-Type: application/json
```
##### Body
```json
{
    "follower": {
        "id": 3,
        "username": "userOne"
    },
    "follow": {
        "id": 28,
        "username": "someUser"
    }
}
```

### Unfollow from anouther user
#### Description
For this resource, an authorized user can unfollow from another user
```
DELETE /users/:username/follow
```
#### Request example
##### Headers
```
DELETE /users/someUser/follow
Content-Type: application/json
Cookie: SESSION
```
##### Body
```json
{
    "follower":{"username":"userOne"},
    "follow":{"username":"someUser"}
}
```
#### Response example
##### Headers
```
HTTP Status: 200 OK
Content-Type: application/json
```
##### Body
```
none
```

## Administration
#### Description
This resource is available only to an authorized user with the administrator role (ROLE_ADMIN).

### Admin's list of users
#### Description
For this resource, an authorized user with the administrator role can get a list of the last registered users```
```
GET /admin/users
```
This resource supports pagination. 
Pagination starts from index zero and by default (i.e. without parameters passed in the request) a list of 15 most recently registration users is returned.
For pagination, you need to add parameters to the request:
```
page: {N} 
size: {N}
(default page=0, size=15)
Example: GET /admin/users?&page=2&size=20
```
#### Request example
##### Headers
```
GET /admin/users
Content-Type: application/json
Cookie: SESSION
```
##### Body
```
none
```
#### Response example
##### Headers
```
HTTP Status: 200 OK
Content-Type: application/json
```
##### Body
```json
[
    {
        "id": 3,
        "username": "admin",
        "createdDate": null,
        "roles": [
            {
                "name": "ROLE_USER"
            },
            {
                "name": "ROLE_ADMIN"
            }
        ]
    },
    {
        "id": 28,
        "username": "user2",
        "createdDate": "12.11.2020 09:14",
        "roles": [
            {
                "name": "ROLE_USER"
            }
        ]
    },
    {
        "id": 27,
        "username": "user1",
        "createdDate": "05.11.2020 08:45",
        "roles": [
            {
                "name": "ROLE_USER"
            }
        ]
    },
    {
        "id": 25,
        "username": "user",
        "createdDate": "29.10.2020 11:56",
        "roles": [
            {
                "name": "ROLE_USER"
            }
        ]
    }
]
```

### Admin's user profile
#### Description
For this resource, an authorized user with the administrator role can get user profile
```
GET /admin/users/:username
```
#### Request example
##### Headers
```
GET /admin/users/user3
Content-Type: application/json
Cookie: SESSION
```
##### Body
```
none
```
#### Response example
##### Headers
```
HTTP Status: 200 OK
Content-Type: application/json
```
##### Body
```json
{
    "id": 27,
    "email": "user@test.ru",
    "username": "user3",
    "privateStatus": false,
    "surname": "",
    "name": "",
    "birthday": null,
    "createdDate": "05.11.2020 08:45",
    "roles": [
        {
            "name": "ROLE_USER"
        }
    ]
}
```

### Delete user
#### Description
For this resource, an authorized user with the administrator role can delete user
Also, together with the deletion of the user, all data associated with him will be deleted, namely: posts, subscriptions, comments
```
DELETE /admin/users/:username
```
#### Request example
##### Headers
```
DELETE /admin/users/user3
Content-Type: application/json
Cookie: SESSION
```
##### Body
```
none
```
#### Response example
##### Headers
```
HTTP Status: 200 OK
Content-Type: application/json
```
##### Body
```json
{
    "id": 28,
    "email": "test22@test.com",
    "username": "test123",
    "privateStatus": false,
    "surname": "",
    "name": "",
    "birthday": null,
    "createdDate": "12.11.2020 09:14",
    "roles": [
        {
            "name": "ROLE_USER"
        }
    ]
}
```
### Admin's edit user profile
#### Description
For this resource, an authorized user with the administrator role can edit user profile
The administrator can also change email, username, roles and all other information
```
PUT /admin/users/:username/settings/profile
```
#### Request example
##### Headers
```
PUT /admin/users/user3/settings/profile
Content-Type: application/json
Cookie: SESSION
```
##### Body
```json
{
    "email":"user@test.ru",
    "username":"user2",
    "surname":"user2Surname",
    "name":"user2Name",
    "birthday":"1999-07-10",
    "privateStatus": false,
    "roles":[
      {
          "id":1,
          "name":"ROLE_USER"
      }
  ]
}
```
#### Response example
##### Headers
```
HTTP Status: 200 OK
Content-Type: application/json
```
##### Body
```json
{
    "id": 27,
    "email": "user@test.ru",
    "username": "user2",
    "privateStatus": false,
    "surname": "user2Surname",
    "name": "user2Name",
    "birthday": "1999-07-10",
    "createdDate": "05.11.2020 08:45",
    "roles": [
        {
            "name": "ROLE_USER"
        }
    ]
}
```

### Admin's edit user password
#### Description
For this resource, an authorized user with the administrator role can edit user password
```
PUT /admin/users/:username/settings/security
```
#### Request example
##### Headers
```
PUT /admin/users/user2/settings/security
Content-Type: application/json
Cookie: SESSION
```
##### Body
```json
{
    "username":"user2",
    "password":"123456newPass",
    "passwordConfirm":"123456newPass"
}
```
#### Response example
##### Headers
```
HTTP Status: 200 OK
Content-Type: application/json
```
##### Body
```json
{
    "id": 27,
    "email": "user@test.ru",
    "username": "user2",
    "privateStatus": false,
    "surname": "user2Surname",
    "name": "user2Name",
    "birthday": "1999-07-10",
    "createdDate": "05.11.2020 08:45",
    "roles": [
        {
            "name": "ROLE_USER"
        }
    ]
}
```

### Admin's list of posts
#### Description
For this resource, an authorized user with the administrator role can get a list of the last registered users```
```
GET /admin/posts
```
This resource supports pagination. 
Pagination starts from index zero and by default (i.e. without parameters passed in the request) a list of 15 most recently created posts is returned.
For pagination, you need to add parameters to the request:
```
page: {N} 
size: {N}
(default page=0, size=15)
Example: GET /admin/posts?&page=2&size=20
```
#### Request example
##### Headers
```
GET /admin/posts
Content-Type: application/json
Cookie: SESSION
```
##### Body
```
none
```
#### Response example
##### Headers
```
HTTP Status: 200 OK
Content-Type: application/json
```
##### Body
```json
[
    {
        "id": 31,
        "author": {
            "id": 3,
            "username": "admin"
        },
        "topic": "Validate form in Vue.js",
        "createdDate": "12.11.2020 13:29",
        "lastUpdatedDate": "19.11.2020 07:23"
    },
    {
        "id": 26,
        "author": {
            "id": 3,
            "username": "admin"
        },
        "topic": "testTopic",
        "createdDate": "29.10.2020 13:12",
        "lastUpdatedDate": "29.10.2020 13:12"
    },
    {
        "id": 7,
        "author": {
            "id": 3,
            "username": "user22"
        },
        "topic": "Anouther test topic",
        "createdDate": "23.10.2020 07:28",
        "lastUpdatedDate": "23.10.2020 07:28"
    },
    {
        "id": 6,
        "author": {
            "id": 3,
            "username": "user5"
        },
        "topic": "new Topic",
        "createdDate": "23.10.2020 07:28",
        "lastUpdatedDate": "14.11.2020 21:08"
    }
]
```

### Admin's delete post
#### Description
For this resource, an authorized user with the administrator role can delete post
```
DELETE /admin/posts/:postId
```
#### Request example
##### Headers
```
DELETE /admin/posts/4
Content-Type: application/json
Cookie: SESSION
```
##### Body
```
none
```
#### Response example
##### Headers
```
HTTP Status: 200 OK
Content-Type: application/json
```
##### Body
```
none
```

## Error response's
#### Description
This section lists possible responses from the server in case of various errors

### Not domain errors
| Server Name | Sub Code | Status | Message | Errors |
| ------ | ------ | ------ | ------ | ------ |
| ACCESS_DENIED | 900 | 403 FORBIDDEN | User: `username` attempted to access the protected URL: `url`  | Access is denied |
| NOT_FOUND | 904 | 404 NOT FOUND | No handler found for `METHOD` `/url`  | No handler found for `METHOD` `/url` |
| METHOD_ARGUMENTS_NOT_VALID | 905 | 400 BAD REQUEST | Method arguments not valid  | Method arguments not valid |
| HTTP_REQUEST_METHOD_NOT_SUPPORTED | 910 | 405 Method Not Allowed | This HTTP method to this url not supported  | Method Not Allowed |
| UNEXPECTED_SERVER_ERROR | 990 | 500 Internal Server Error | Unexpected server error `error`, Msg: `message`  | Unexpected server error |
#### Example
##### Headers
```
HTTP Status: 404 Not Found
Content-Type: application/json
```
##### Body
```json
{
    "status": "NOT_FOUND",
    "subCode": 904,
    "message": "No handler found for GET /foo",
    "errors": [
        "No handler found for GET /foo"
    ]
}
```

### User errors
| Server Name | Sub Code | Status | Message | Errors |
| ------ | ------ | ------ | ------ | ------ |
| USER_NOT_FOUND | 1001 | 409 CONFLICT | User not found, username: `username` | UserException |
| USER_NOT_AUTHORIZED | 1002 | 409 CONFLICT | User is not authorized | UserException |
| USER_ALREADY_IS_FOLLOW | 1003 | 409 CONFLICT | You are already following this user | UserException |
| USER_NOT_FOLLOW | 1004 | 409 CONFLICT | Yoa are not following this user | UserException |

#### Example
##### Headers
```
HTTP Status: 409 CONFLICT
Content-Type: application/json
```
##### Body
```json
{
    "status": "CONFLICT",
    "subCode": 1001,
    "message": "User not found | username: someUser",
    "errors": [
        "UserException"
    ]
}
```

### Registration and User editing errors
| Server Name | Sub Code | Status | Message | Errors |
| ------ | ------ | ------ | ------ | ------ |
| EMAIL_IS_ALREADY_USED | 1005 | 409 CONFLICT | User with this email is already registered | UserException |  
| USERNAME_IS_ALREADY_USED | 1006 | 409 CONFLICT | User with this username is already registered | UserException |
| PASSWORDS_ARE_NOT_EQUAL | 1007 | 409 CONFLICT | Password and password confirm do not match | UserException |

#### Example
##### Headers
```
HTTP Status: 409 CONFLICT
Content-Type: application/json
```
##### Body
```json
{
    "status": "CONFLICT",
    "subCode": 1005,
    "message": "User with this email is already registered | email: example@email.com",
    "errors": [
        "UserException"
    ]
}
```

### Post errors
| Server Name | Sub Code | Status | Message | Errors |
| ------ | ------ | ------ | ------ | ------ |
| UNAUTHORIZED_USER | 2001 | 409 CONFLICT | Unauthorized user | PostException |
| POST_NOT_FOUND | 2002 | 409 CONFLICT | Post with this ID is not found user | PostException |
| INVALID_SEARCH_ARGUMENT | 2003 | 409 CONFLICT | Invalid search argument | PostException |
| USER_IS_NOT_AUTHOR | 2004 | 409 CONFLICT | Authorized user is not the author of the post | PostException |

#### Example
##### Headers
```
HTTP Status: 409 CONFLICT
Content-Type: application/json
```
##### Body
```json
{
    "status": "CONFLICT",
    "subCode": 2003,
    "message": "Invalid search argument | topic: null",
    "errors": [
        "UserException"
    ]
}
```
### Comment errors
| Server Name | Sub Code | Status | Message | Errors |
| ------ | ------ | ------ | ------ | ------ |
| UNAUTHORIZED_USER | 2101 | 409 CONFLICT | Unauthorized user | CommentException |
| POST_NOT_FOUND | 2102 | 409 CONFLICT | Post with this ID is not found | CommentException |
| COMMENT_NOT_FOUND | 2103 | 409 CONFLICT | Comment with this ID is not found | CommentException |
| USER_IN_NOT_AUTHOR | 2104 | 409 CONFLICT | Current user not is author this comment | CommentException |

#### Example
##### Headers
```
HTTP Status: 409 CONFLICT
Content-Type: application/json
```
##### Body
```json
{
    "status": "CONFLICT",
    "subCode": 2102,
    "message": "Post with this ID is not found | postId: 6739",
    "errors": [
        "UserException"
    ]
}
```

### Tag errors
| Server Name | Sub Code | Status | Message | Errors |
| ------ | ------ | ------ | ------ | ------ |
| TAG_NOT_FOUND | 2201 | 409 CONFLICT | Tag with this name not found | TagException |
| INVALID_SEARCH_ARGUMENT | 2202 | 409 CONFLICT | Invalid tag name | TagException |
| USER_IS_NOT_ADMIN | 2203 | 409 CONFLICT | Current authorized user haven't role ADMIN | TagException |

#### Example
##### Headers
```
HTTP Status: 409 CONFLICT
Content-Type: application/json
```
##### Body
```json
{
    "status": "CONFLICT",
    "subCode": 2201,
    "message": "Tag with this name not found | tag: someTag",
    "errors": [
        "UserException"
    ]
}
```



