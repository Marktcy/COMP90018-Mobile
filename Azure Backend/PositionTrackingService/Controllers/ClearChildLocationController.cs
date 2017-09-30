using System.Web.Http;
using System.Data.SqlClient;
using System;
using System.Text;
using Microsoft.Azure.Mobile.Server.Config;

namespace PositionTrackingService.Controllers
{
    [MobileAppController]
    public class ClearChildLocationController : ApiController
    {
        public void Post()
        {
            SqlConnectionStringBuilder builder = new SqlConnectionStringBuilder();
            builder.DataSource = "positionserver.database.windows.net";
            builder.UserID = "shixunl";
            builder.Password = "lsx_1988";
            builder.InitialCatalog = "location";

            using (SqlConnection connection = new SqlConnection(builder.ConnectionString))
            {
                connection.Open();
                StringBuilder sb = new StringBuilder();
                sb.Append("DELETE FROM ChildLocations");
                String sql = sb.ToString();

                using (SqlCommand command = new SqlCommand(sql, connection))
                {
                    command.ExecuteReader();
                }
            }
        }
    }
}
