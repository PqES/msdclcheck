using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;
using Controller;

namespace NewsletterMicroservice
{
    public class Startup
    {
        // This method gets called by the runtime. Use this method to add services to the container.
        // For more information on how to configure your application, visit https://go.microsoft.com/fwlink/?LinkID=398940
        public void ConfigureServices(IServiceCollection services)
        {
        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        public void Configure(IApplicationBuilder app, IHostingEnvironment env, ILoggerFactory loggerFactory)
        {
            loggerFactory.AddConsole();

            if (env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
            }

            app.Run(async (context) =>
            {
                string method = context.Request.Query["method"].FirstOrDefault();
                if(method != null){
                    if(method == "Publish"){
                        await context.Response.WriteAsync(""+Newsletter.Publish());
                    }else if(method == "Subscribe"){
                        string cpf = context.Request.Query["cpf"].FirstOrDefault();
				        string email = context.Request.Query["email"].FirstOrDefault();
                        await context.Response.WriteAsync(""+Newsletter.Subscribe(cpf, email));
                    }else{
                        await context.Response.WriteAsync("False");    
                    }
                }else{
                    await context.Response.WriteAsync("False");
                }
            });
        }
    }
}
