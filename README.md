# UberAssignment

Using Flikr api to get load images in recycle view.
used memory cache and disc cache to improve the performace of the image loading into recycle view .
Separated the data layer(i.e api data) from the view  layer(i.e activity) using interface,similar to MVP.
pagination support with each page containing 9 item 
displaying images in the grid formate with 3 columns in each row
search view to search image related to any keyword.
not using any third party library.
Default query for search view is "flower" so first time when you will open app it will show images realted to flower keyword after that using search view you can change the keyword and press search in the softkey pad to get the results.
