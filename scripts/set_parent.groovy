import qupath.lib.scripting.QP

def parentobj = QP.getSelectedObject()
def dets = QP.getDetectionObjects()
def adddets = dets.findAll {
    parentobj.getROI().contains(it.getROI().getCentroidX(),it.getROI().getCentroidY())
}
parentobj.addChildObjects(adddets)
