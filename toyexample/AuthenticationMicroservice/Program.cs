using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Hosting;

namespace AuthenticationMicroservice
{
    public class Program
    {
        public static void Main(string[] args)
        {
            var host = new WebHostBuilder()
                .UseKestrel()
                .UseContentRoot(Directory.GetCurrentDirectory())
                .UseIISIntegration()
                .UseStartup<Startup>()
<<<<<<< HEAD
                .UseUrls("")
=======
                .UseUrls("http://localhost:5000")
>>>>>>> 9b1e39935e512fafd6d46f41201a1dfd7db9fb0e
                .Build();

            host.Run();
        }
    }
}
