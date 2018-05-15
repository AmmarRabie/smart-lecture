const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);


exports.sendNotification = functions.database.ref("/groupNs/{group_id}/{notification_id}")
.onWrite((change, context) =>
{
	const groupId = context.params.group_id;
	const notificationId = context.params.notification_id;
	console.log("sendNotification started  wiht params = " + groupId + "____" + notificationId);
	
	return admin.database().ref("groupNs/" + groupId + "/" + notificationId).once("value", function(queryResult) {
		const mes = queryResult.val().message;
		const tit = queryResult.val().title;
		console.log("title fetched with: " + mes);
		console.log("message fetched with: " + tit);
		const payload = {
			notification: {
				title: tit,
				body: mes,
			},
			data: {
				group_id: groupId,
				notification_id: notificationId,
			},
			topic: groupId
		}
		return admin.messaging().send(payload);
	});
});
	



