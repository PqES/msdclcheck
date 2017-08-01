using System;
using System.IO;
using MimeKit;
using MailKit.Net.Smtp;
using MailKit.Security;
using MsNewsletter.Entities;
using MsNewsletter.DAO;
using System.Collections.Generic;
using System.Linq;
namespace MsNewsletter.Email{
	public class EmailManager{

        public static void sendEmail(User user){
            Console.WriteLine("fake email to "+ user.Email);
            var message = new MimeMessage ();
			message.From.Add (new MailboxAddress ("Elder", "elder_jr1995@hotmail.com"));
			message.To.Add (new MailboxAddress ("User - CPF: " + user.Cpf, user.Email));
			message.Subject = "ToyExample - Publish";
			message.Body = new TextPart ("plain") {
				Text = "There are a new products in ToyExample"
			};
			try{
				using (var client = new SmtpClient ()) {
				client.ServerCertificateValidationCallback = (s,c,h,e) => true;
				client.Connect ("smtp-mail.outlook.com", 587, false);
				client.AuthenticationMechanisms.Remove ("XOAUTH2");
				client.Authenticate ("fake@fake.com", "fake");
				client.Send (message);
				client.Disconnect (true);
			}
			}catch(Exception ex){
				Console.WriteLine("Fail to send email to "+user.Email);
				Console.WriteLine(ex.Message);
			}
        }

		public static void sendEmailToAll(){
			LinkedList<User> users = UserDAO.GetAllUsers();
			foreach(var u in users){
				EmailManager.sendEmail(u);
			}
		}
	}
}