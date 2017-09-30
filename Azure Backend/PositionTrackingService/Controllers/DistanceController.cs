using System.Web.Http;
using Microsoft.Azure.Mobile.Server.Config;

namespace PositionTrackingService.Controllers
{
    [MobileAppController]
    public class DistanceController : ApiController
    {
        private const double EARTH_RADIUS = 6378.137;

        // GET api/Distance
        public bool Get(double long1, double lat1, double long2, double lat2, int radius)
        {
            double radlat1 = Rad(lat1);
            double radlat2 = Rad(lat2);
            double a = radlat1 - radlat2;
            double b = Rad(long1) - Rad(long2);
            double s = 2 * System.Math.Asin(System.Math.Sqrt(System.Math.Pow(System.Math.Sin(a/2),2) +
                System.Math.Cos(radlat1)*System.Math.Cos(radlat2)*System.Math.Pow(System.Math.Sin(b/2),2)));

            s = s * EARTH_RADIUS;
            s = System.Math.Round(s * 10000) / 10000;

            if (s < radius)
                return false;
            else
                return true;
        }

        private double Rad(double d)
        {
            return d * System.Math.PI / 180.0;
        }
    }
}
