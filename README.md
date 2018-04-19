# Handling-Screen-Rotation-with-networking-with-pagination
Handles screen rotation even in middle of an ongoing network call.Loads data using the concept of pagination into the recycler view.
currently we are using github api: https://developer.github.com/v3/search/#search-users for network calls
It searches the github users from search view and loads the data as received.
To make the search faster, it also uses caching, if the data requested is already present then it is loade from the cahce itself.
Plus it also uses pagination, where one page of data is loade at a time, incase user scrolls bottom then next pae data is loaded.
