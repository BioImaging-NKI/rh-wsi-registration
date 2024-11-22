import net.imglib2.realtransform.InvertibleRealTransformSequence
import org.locationtech.jts.geom.CoordinateSequenceFilter
import qupath.ext.warpy.Warpy
import qupath.lib.objects.PathObject
import qupath.lib.scripting.QP
import java.nio.file.Paths
import java.util.UUID

// For some reason this takes about 51 seconds even for only 18k objects

def filename = 'transform.json'
def transformfile = Paths.get(QP.getProject().getPath().parent as String, filename).toFile()
InvertibleRealTransformSequence mytransform = Warpy.getRealTransform(transformfile) as InvertibleRealTransformSequence
def myinverstransform = mytransform.inverse()  // codex -> HE is inverse
def to_transform = QP.getSelectedObjects()
to_transform.each { PathObject selectedobj ->
    selectedobj.setName(selectedobj.getID() as String)
    selectedobj.getChildObjects().each { childobj ->
        childobj.setName(childobj.getID() as String)
    }
}
CoordinateSequenceFilter transformer = Warpy.getJTSFilter(myinverstransform)
Collection<PathObject> from_transform = []
to_transform.each{ PathObject selectedobj ->
    from_transform.add(Warpy.transformPathObject(selectedobj, transformer, false, true))
    Collection<PathObject> children = []
    selectedobj.getChildObjects().each { childobj ->
        children.add(Warpy.transformPathObject(childobj, transformer, false, true))
    }
    selectedobj.addChildObjects(children)
}
//QP.removeObjects(to_transform, false)
from_transform.each{PathObject selectedobj ->
print(selectedobj.getName())
    selectedobj.ID = UUID.fromString(selectedobj.getName())
    selectedobj.getChildObjects().each { childobj ->
        childobj.ID = UUID.fromString(childobj.getName())
    }
}
QP.addObjects(from_transform)
