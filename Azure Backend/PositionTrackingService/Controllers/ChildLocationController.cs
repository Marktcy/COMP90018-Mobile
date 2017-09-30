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
    public class ChildLocationController : TableController<ChildLocation>
    {
        protected override void Initialize(HttpControllerContext controllerContext)
        {
            base.Initialize(controllerContext);
            PositionTrackingContext context = new PositionTrackingContext();
            DomainManager = new EntityDomainManager<ChildLocation>(context, Request);
        }

        // GET tables/Location
        public IQueryable<ChildLocation> GetAllChildLocations()
        {
            return Query();
        }

        // GET tables/Location/48D68C86-6EA6-4C25-AA33-223FC9A27959
        public SingleResult<ChildLocation> GetChildLocation(string id)
        {
            return Lookup(id);
        }

        // PATCH tables/Location/48D68C86-6EA6-4C25-AA33-223FC9A27959
        public Task<ChildLocation> PatchChildLocation(string id, Delta<ChildLocation> patch)
        {
            return UpdateAsync(id, patch);
        }

        // POST tables/Location
        public async Task<IHttpActionResult> PostChildLocation(ChildLocation loc)
        {
            ChildLocation current = await InsertAsync(loc);
            return CreatedAtRoute("Tables", new { id = current.Id }, current);
        }

        // DELETE tables/Location/48D68C86-6EA6-4C25-AA33-223FC9A27959
        public Task DeleteChildLocation(string id)
        {
            return DeleteAsync(id);
        }
    }
}