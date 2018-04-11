var MongoClient = require('mongodb').MongoClient;
var bodyParser = require('body-parser');
var BSON = require('bson');
const http = require('http');
const express = require('express');
const app = express();

var uri = "mongodb://461L_QRU:test1234@qru-shard-00-00-wlzab.mongodb.net:27017,qru-shard-00-01-wlzab.mongodb.net:27017,qru-shard-00-02-wlzab.mongodb.net:27017/test?ssl=true&replicaSet=QRU-shard-0&authSource=admin";

String.prototype.replaceAt=function(index, replacement) {
    return this.substr(0, index) + replacement+ this.substr(index + replacement.length);
}
String.prototype.deleteThatShit=function(index) {
  if(index == this.length){
    return this.substr(0, index - 1);
  } else {
    return this.substr(0, index - 1) + this.substr(index + 1, this.length - 1);
  }
}

//app.use(express.bodyParser());
//app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());
//app.use(app.router);
app.use(bodyParser.text({ type: String }));

app.post('/route1', function (req, res) {
  MongoClient.connect(uri, function(err, db) {
    if (err) throw err;
    console.log("Database created!");
    var dbo = db.db("QRU");
    var query = req.body;

    /*var hold = JSON.stringify(query);

    var n = hold.search("_id");

    console.log(n);

    if(n != -1){
      hold = hold.deleteThatShit(0);
      hold = hold.deleteThatShit(hold.length);
      hold = hold.replace("\\", "");
      hold = hold.replace("\\", "");
      hold = hold.replace("\\", "");
      hold = hold.replace("\\", "");
      hold = hold.replace("\\", "");
    	hold = hold.replaceAt(hold.search(":") + 1, " "); 
			//hold = hold.replaceAt(hold.search(":") + 2, " "); 
      hold = hold.deleteThatShit(hold.length - 2);
    }

    console.log(hold);*/
    var jsoned = JSON.parse(query);
    console.log(jsoned);

    /*var n = jsoned.search("_id");

    if(n != -1){
    	_id.replaceAt(_id.search("'"), ""); 
			_id.replaceAt(_id.search("'"), ""); 
    }*/

    //console.log(jsoned._id);
    dbo.collection("customers").find(jsoned).toArray(function(err, result) {
      if (err) throw err;
      console.log(result);
      res.send(result);
      db.close();
    }); 
  });
});

app.post('/route2', function (req, res) {
  MongoClient.connect(uri, function(err, db) {
    if (err) throw err;
    console.log("Database created!");
    var dbo = db.db("QRU");
    var myobj = req.body;
    console.log(myobj);
    var jsoned = JSON.parse(myobj);
    console.log(jsoned);
    dbo.collection("customers").insertOne(jsoned, function(err, res) {
      if (err) throw err;
      console.log("1 document inserted");
      db.close();
    }); 
    res.send("1 document inserted");
  });
});

app.post('/route2/:id', function (req, res) {
  MongoClient.connect(uri, function(err, db) {
    if (err) throw err;
    console.log("Database created!");
    var dbo = db.db("QRU");
    var myobj = req.body;
    console.log(myobj);
    var jsoned = JSON.parse(myobj);
    console.log(jsoned);
    dbo.collection("customers").insertOne(jsoned, function(err, res) {
      if (err) throw err;
      console.log("1 document inserted");
      db.close();
    }); 
    res.send("1 document inserted");
  });
});


app.post('/route3/:id', function (req, res) {
  MongoClient.connect(uri, function(err, db) {
    if (err) throw err;
			console.log("Connected to database");
			var dbo = db.db("QRU");
			var id = req.params.id;
			console.log('Updating DB at: ' + id);
			var update = req.body;
			var jsoned = JSON.parse(update);
			console.log(jsoned);
			dbo.collection("customers").update({'_id':new BSON.ObjectID(id)}, jsoned, {multi:true}, function(err, result) {
          if (err) {
              console.log('Error updating: ' + err);
              res.send({'error':'An error has occurred'});
          } else {
              console.log('' + result + ' document(s) updated');
              res.send(result);
          }				
			});
    
  });
});


/*MongoClient.connect(uri, function(err, db) {
  if (err) throw err;
  console.log("Database created!");
 	var dbo = db.db("QRU");
 	var query = { address: "Highway 37" };
  dbo.collection("customers").find(query).toArray(function(err, result) {
    if (err) throw err;
    console.log(result);
    db.close();
  }); 

*/

  /*var myobj = { name: "Company Inc", address: "Highway 37" };
 	dbo.collection("customers").insertOne(myobj, function(err, res) {
    if (err) throw err;
    console.log("1 document inserted");
    db.close();
  });  */
  /*dbo.createCollection("user", function(err, res) {
    if (err) throw err;
    console.log("Collection created!");
    db.close();
  }); */
//});

app.listen(8080);
console.log('Listening on port 8080...');
