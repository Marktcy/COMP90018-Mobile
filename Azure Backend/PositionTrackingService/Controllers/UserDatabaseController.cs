using System.Linq;
using System.Threading.Tasks;
using System.Web.Http;
using System.Web.Http.Controllers;
using System.Web.Http.OData;
using Microsoft.Azure.Mobile.Server;
using PositionTrackingService.DataObjects;
using PositionTrackingService.Models;

namespace PositionTrackingService.Controllers
{
    public class UserDatabaseController : TableController<UserDatabase>
    {
        protected override void Initialize(HttpControllerContext controllerContext)
        {
            base.Initialize(controllerContext);
            PositionTrackingContext context = new PositionTrackingContext();
            DomainManager = new EntityDomainManager<UserDatabase>(context, Request);
        }

        // GET tables/Location
        public IQueryable<UserDatabase> GetAllUserDatabases()
        {
            return Query();
        }

        // GET tables/Location/48D68C86-6EA6-4C25-AA33-223FC9A27959
        public SingleResult<UserDatabase> GetUserDatabase(string id)
        {
            return Lookup(id);
        }

        // PATCH tables/Location/48D68C86-6EA6-4C25-AA33-223FC9A27959
        public Task<UserDatabase> PatchUserDatabase(string id, Delta<UserDatabase> patch)
        {
            return UpdateAsync(id, patch);
        }

        // POST tables/Location
        public async Task<IHttpActionResult> PostUserDatabase(UserDatabase loc)
        {
            UserDatabase current = await InsertAsync(loc);
            return CreatedAtRoute("Tables", new { id = current.Id }, current);
        }

        // DELETE tables/Location/48D68C86-6EA6-4C25-AA33-223FC9A27959
        public Task DeleteUserDatabase(string id)
        {
            return DeleteAsync(id);
        }
    }
}