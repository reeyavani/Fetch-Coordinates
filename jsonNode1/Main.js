var NodeGeocoder = require('node-geocoder');
var options = {
	provider: 'google',
 
 	 // Optional depending on the providers 
 	 httpAdapter: 'https', // Default 
  	apiKey: 'AIzaSyCB0rU5C6rcQWCxueV2DwBXOKKCAS77Z7A', // for Mapquest, OpenCage, Google Premier 
  	formatter: null         // 'gpx', 'string', ... 
};
 
var geocoder = NodeGeocoder(options);
var express = require('express'), bodyParser = require('body-parser');
var app = express();
app.use(bodyParser.json());

app.post('/', function(request, response){
	console.log(request.body.city);      // your JSON
	//response.send(request.body.city);    // echo the result back
	geocoder.geocode(request.body.city).then(function(res) {
    		console.log(res);
		var arr = res;
		var table  = { adddata:[]}
		for (var i = 0; i < arr.length; i++) 
		{
 
 			console.log(arr[i]["latitude"]);
  			console.log(arr[i]["longitude"]);
      			table.adddata.push({"lat":arr[i]["latitude"],"long":arr[i]["longitude"]});
			console.log(table);
			var json = JSON.stringify(table);
			//response.write(json);
			response.send(json); 
		}


    
	}).catch(function(err) {
		console.log(err);
	  });

});
app.listen(3000);
console.log("Server started at 3000");