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
    public class ParentLocationController : TableController<ParentLocation>
    {
        protected override void Initialize(HttpControllerContext controllerContext)
        {
            base.Initialize(controllerContext);
            PositionTrackingContext context = new PositionTrackingContext();
            DomainManager = new EntityDomainManager<ParentLocation>(context, Request);
        }
        
        public IQueryable<ParentLocation> GetAllParentLocations()
        {
            return Query();
        }
        
        public SingleResult<ParentLocation> GetParentLocation(string id)
        {
            return Lookup(id);
        }
        
        public Task<ParentLocation> PatchParentLocation(string id, Delta<ParentLocation> patch)
        {
            return UpdateAsync(id, patch);
        }
        
        public async Task<IHttpActionResult> PostParentLocation(ParentLocation pLoc)
        {
            ParentLocation current = await InsertAsync(pLoc);
            return CreatedAtRoute("Tables", new { id = current.Id }, current);
        }
        
        public Task DeleteBParentLocation(string id)
        {
            return DeleteAsync(id);
        }
    }
}