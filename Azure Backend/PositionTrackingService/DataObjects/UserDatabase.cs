using Microsoft.Azure.Mobile.Server;

namespace PositionTrackingService.DataObjects
{
    public class UserDatabase : EntityData
    {
        public string Name { get; set; }

        public string Email { get; set; }

        public string Password { get; set; }

        public bool Role { get; set; }
    }
}