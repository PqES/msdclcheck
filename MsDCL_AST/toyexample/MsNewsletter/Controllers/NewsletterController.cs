using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using MsNewsletter.Entities;
using MsNewsletter.DAO;
using MsNewsletter.Email;

namespace MsNewsletter.Controllers
{
    [Route("api/[controller]")]
    public class NewsletterController : Controller
    {
        // api/newsletter/subscribe
        [HttpPost("subscribe")]
        public bool Subscribe(string cpf, string email)
        {
            return UserDAO.RegisterUser(new User(cpf, email));
        }

        // api/newsletter/publish
        [HttpGet("publish")]
        public bool Publish()
        {
            LinkedList<User> users = UserDAO.GetAllUsers();
			foreach(User user in users){
				EmailManager.sendEmail(user);
			}
			return true;
        }
    }
}
