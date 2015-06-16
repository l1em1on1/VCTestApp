# VCTestApp
Simple REST API app

Application does fetch the offers list from remote JSON file. 

So for that purpose I have used **com.squareup.okhttp:okhttp**, which is simple and does the task well. It also provides a simple file cache system that I have used, so in case we don't have internet it will use it instead. I could have used sqlite to store data after I downoload it and parse, but I don't think it is required for the task. But this could be other solution, which makes sense for bigger data lists, which could also be filtered straight with the query. It also comes with its own async request handler, that you can cancel at any time etc.

To show the remote images I have used **com.github.bumptech.glide:glide** which is really good library. It allows you to insert images to the list items without rendering issues, and also provides cache, so the image is pulled once. The other nice thing is it always uses the best image size, which prevent from waisting memory, and limit the memory leaks. Glide also uses **com.github.bumptech.glide:okhttp-integration** which let me use just one http client, I have choosen before. And many more, [Glide recomendation info.](http://inthecheesefactory.com/blog/get-to-know-glide-recommended-by-google/en)

For the JSON parsing, I decided to use **com.fasterxml.jackson.core** which looks like is really optimized parser.

So the json is being downloaded asynchronously, if there is internet it pulls it online, if no connectivity it uses previously cached response (if was any). After we have JSON data, there is AsyncTask called that parse the json, and as a callback update List adapter.

App works on phones and tablets, and should work across all different screen sizes and combinations. 

When we select offer to see the details screen the location will be checked, and if there is any set, map will be updated with the nearest offer location to the current device location. If not map won't update. There could be some extra message if there is no location or so, but I think this is explanatory enought. Once fetched location is kept in the memory so any other updates will be instant.

