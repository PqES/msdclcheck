using System;

namespace Entities{
	public class User{
		private string name;
		private string password;
		public string Name{get{return name;}}
		public String Password{get{return password;}}

		public User(string name, string password){
			this.name = name;
			this.password = password;
		}
	}
}
