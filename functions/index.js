const functions = require('firebase-functions');
const admin = require('firebase-admin');
    
	admin.initializeApp(functions.config().firestore);

exports.deleteEntries = (event, callback) => {
	//get the current time
  var now = Date.now();

	//select old needs


	
  const snapshot = admin.firestore().document("needs").orderByChild('timeToLive').where('timeToLive', '<',now).once('value');
  // create a map with all children that need to be removed
 const updates = {};
  snapshot.forEach(child => {
    updates[child.key] = null;
  });
  // execute all updates in one go and return the result to end the function
  return ref.update(updates);
};

