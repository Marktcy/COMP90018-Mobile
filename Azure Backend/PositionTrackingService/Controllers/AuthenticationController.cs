using System.Web.Http;
using System.Data.SqlClient;
using System;
using System.Text;
using Microsoft.Azure.Mobile.Server.Config;

namespace PositionTrackingService.Controllers
{
    [MobileAppController]
    public class AuthenticationController : ApiController
    {
        // GET api/Authentication
        public CheckEmail Get(String email, String password, String type)
        {
            CheckEmail results = new CheckEmail
            {
                Email = email,
                Password = password,
                Type = type
            };
            SqlConnectionStringBuilder builder = new SqlConnectionStringBuilder();
            builder.DataSource = "positionserver.database.windows.net";
            builder.UserID = "shixunl";
            builder.Password = "lsx_1988";
            builder.InitialCatalog = "location";
            
            using (SqlConnection connection = new SqlConnection(builder.ConnectionString))
            {
                connection.Open();
                StringBuilder sb = new StringBuilder();
                sb.Append("SELECT * FROM UserDatabases");
                String sql = sb.ToString();

                using (SqlCommand command = new SqlCommand(sql, connection))
                {
                    using (SqlDataReader reader = command.ExecuteReader())
                    {
                        if (type.Equals("isEmailExist"))
                        {
                            while (reader.Read())
                            {
                                if (email.Equals(reader.GetString(2)))
                                {
                                    results.Result = "Emailexist";
                                    return results;
                                }
                            }
                            results.Result = "NotExist";
                        }

                        if (type.Equals("checkPassword"))
                        {
                            while (reader.Read())
                            {
                                if (email.Equals(reader.GetString(2)) && password.Equals(reader.GetString(3)))
                                {
                                    if (reader.GetBoolean(4) == true)
                                        results.Result = "Parent";
                                    else
                                        results.Result = "Children";

                                    return results;
                                }
                            }
                            results.Result = "Passwordfailed";
                        }
                        return results;
                    }
                }
            }
        }

        public class CheckEmail
        {
            public String Email { get; set;  }
            public String Password { get; set; }
            public String Type { get; set; }
            public String Result { get; set; }
        }
    }
}
