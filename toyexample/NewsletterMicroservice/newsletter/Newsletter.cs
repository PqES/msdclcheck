using System;
using System.IO;
using System.Collections.Generic;
using DAO;
using Entities;
using Email;
namespace Controller{
	public class Newsletter{

		public static bool Subscribe (string cpf, string email){
			return UserDAO.RegisterUser(new User(cpf, email));
		}

		public static bool Publish(){
			LinkedList<User> users = UserDAO.GetAllUsers();
			foreach(User user in users){
				EmailManager.sendEmail(user);
			}
			return true;
		}
	}
}