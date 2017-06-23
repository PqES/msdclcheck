using System;
using System.IO;
using MimeKit;
using MailKit.Net.Smtp;
using MailKit.Security;
using Entities;
namespace Email{
	public class EmailManager{

        public static bool sendEmail(User user){

            Console.WriteLine("fake email to "+ user.Email);
            var message = new MimeMessage ();
			message.From.Add (new MailboxAddress ("Elder", "elder_jr1995@hotmail.com"));
			message.To.Add (new MailboxAddress ("User - CPF: " + user.Cpf, user.Email));
			message.Subject = "ToyExample - Publish";
			message.Body = new TextPart ("plain") {
				Text = "There are a new products in ToyExample"
			};
			using (var client = new SmtpClient ()) {
				client.ServerCertificateValidationCallback = (s,c,h,e) => true;
				client.Connect ("smtp-mail.outlook.com", 587, false);
				client.AuthenticationMechanisms.Remove ("XOAUTH2");
				client.Authenticate ("fakeemail", "fakepassword");
				client.Send (message);
				client.Disconnect (true);
			}
            return true;
        }
	}
}