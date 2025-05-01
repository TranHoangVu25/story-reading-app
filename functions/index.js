const functions = require("firebase-functions");
const sg = require("@sendgrid/mail");

sg.setApiKey(functions.config().sendgrid.key);

exports.sendOtpEmail = functions.https.onCall(async (data) => {
        const email = data.email;
        if (!email) {
            throw new functions.https.HttpsError(
                "invalid-argument",
                "Email trống",
            );
        }
        // Sinh OTP 6 chữ số
        const otp = Math.floor(100000 + Math.random() * 900000).toString();

        // Gửi mail
        await sg.send({
            to: email,
            from: functions.config().sendgrid.from,
            subject: "Mã OTP",
            html: `<p>OTP của bạn: <b>${otp}</b></p>`,
        });

        return {otp};
    },
);
