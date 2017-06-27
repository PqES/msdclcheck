using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using MsAuthenticate.DAO;
using MsAuthenticate.Entities;
namespace MsAuthenticate.Controllers
{
    [Route("api/[controller]")]
    public class AuthenticateController : Controller
    {
        // POST api/authenticate
        [HttpPost]
        public bool Authenticate(string username, string password)
        {
            User user = UserDAO.GetUserByUsername(username);
			if(user != null){
				return user.Password == password;
			}else{
				return false;
			}   
        }
    }
}
