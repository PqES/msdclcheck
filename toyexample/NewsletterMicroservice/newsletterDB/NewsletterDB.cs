using System;
using System.IO;
using System.Collections.Generic;
using Entities;
namespace DAO{
	public class UserDAO{

        private static StreamWriter OpenDBToWrite(){
            try{
                StreamWriter sw = File.AppendText("users.txt");
                return sw;
            }catch(Exception e){
                Console.WriteLine("The file could not be write:");
				Console.WriteLine(e.Message);
                return null;
            }
        }

        private static StreamReader OpenDBToRead(){
            FileStream file = new FileStream("users.txt", FileMode.Open);
			try{
				StreamReader sr = new StreamReader(file);
				return sr;
			}
			catch (Exception e){
				Console.WriteLine("The file could not be read:");
				Console.WriteLine(e.Message);
				return null;
			}
        }

        
		public static bool RegisterUser (User user){
            StreamWriter sw = OpenDBToWrite();
            if(sw != null){
                sw.WriteLine(user.Cpf+";"+user.Email);
                sw.Flush();
                return true;
            }else{
                return false;
            }
		}

        public static LinkedList<User> GetAllUsers(){
            LinkedList<User> users = new LinkedList<User>();
            StreamReader sr = OpenDBToRead();
            if(sr != null){
                while(sr.Peek() >= 0){
                    string line = sr.ReadLine();
                    string[] splitedLine = line.Split(';');
                    users.AddLast(new User(splitedLine[0], splitedLine[1]));
                }
            }
            return users;
        }
	}
}