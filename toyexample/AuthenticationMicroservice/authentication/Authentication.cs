using System;
using System.IO;
using DAO;
using Entities;
namespace Controller
{
	public class Authentication
	{
		public static bool Authenticate (string username, string password){
			User user = UserDAO.GetUserByUsername(username);
			if(user != null){
				return user.Password == password;
			}else{
				return false;
			}
		}
	}
}
