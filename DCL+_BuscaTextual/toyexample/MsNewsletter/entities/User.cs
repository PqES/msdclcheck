using System;
using System.IO;

namespace MsNewsletter.Entities{
	public class User{

        private string cpf;
        private string email;

        public string Cpf{get{return cpf;}}
        public string Email{get{return email;}}
        
        public User(string cpf, string email){
            this.cpf = cpf;
            this.email = email;
        }
	}
}