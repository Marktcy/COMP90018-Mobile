using Microsoft.Azure.Mobile.Server;

namespace PositionTrackingService.DataObjects
{
    public class Location : EntityData
    {
        public double Longtitude { get; set; }

        public double Latitude { get; set; }
    }
}