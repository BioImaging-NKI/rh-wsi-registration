import qupath.lib.images.servers.ImageServer
import qupath.lib.regions.RegionRequest
import qupath.lib.roi.RectangleROI
import qupath.lib.scripting.QP

import java.awt.image.BufferedImage
import java.nio.file.FileSystems
RECT_SIZE = 128  // change to desired size
def basepath = QP.getProject().getPath().getParent()
ImageServer<BufferedImage> myserver = QP.getCurrentServer() as ImageServer<BufferedImage>
def detections = QP.getDetectionObjects()
def celltype = "CD3e+"  // change to desired celltype
detections.forEach {
        if (it.getPathClass().toString()==celltype) {
                def uuid = it.getID().toString()
                def x = it.getROI().getCentroidX()
                def y = it.getROI().getCentroidY()
                def rect_roi = new RectangleROI(x - RECT_SIZE / 2, y - RECT_SIZE / 2, RECT_SIZE, RECT_SIZE, null)
                def region = RegionRequest.createInstance(myserver.getPath(), 1, rect_roi)
                def outpth = FileSystems.getDefault().getPath(basepath as String, uuid + '.jpg')
                QP.writeImageRegion(myserver, region, outpth as String)
        }
}
