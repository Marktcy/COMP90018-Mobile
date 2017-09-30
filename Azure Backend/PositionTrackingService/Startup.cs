using Microsoft.Owin;
using Owin;

[assembly: OwinStartup(typeof(PositionTrackingService.Startup))]

namespace PositionTrackingService
{
    public partial class Startup
    {
        public void Configuration(IAppBuilder app)
        {
            ConfigureMobileApp(app);
        }
    }
}