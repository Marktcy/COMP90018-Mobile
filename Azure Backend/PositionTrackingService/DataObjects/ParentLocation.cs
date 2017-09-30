using Microsoft.Azure.Mobile.Server;

namespace PositionTrackingService.DataObjects
{
    public class ParentLocation : EntityData
    {
        public double Longtitude { get; set; }

        public double Latitude { get; set; }

        public int Radius { get; set; }
    }
}