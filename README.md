# CODEX H&E Guide
Guide for using QuPath, FIJI and Warpy to work with CODEX and H&E data of whole slide images.

## Data preparation
1. Crop relevant ROI in the H&E and convert to bioformats compatible 24-bit RGB (ome.tif) with LZW compression using [script 1](scripts/export_HE.groovy), this is necessary for the Warpy image registration.
2. Extract DAPI channel from qptiff CODEX to 8-bit ome.tif, using [script 2](scripts/extract_8bit_DAPI.groovy).

## Transformation
We use the [QuPath extension Warpy](https://github.com/BIOP/qupath-extension-warpy) and follow the steps in [the guide](https://imagej.net/plugins/bdv/warpy/warpy-extension). The registration results in a transform.json file.

Extra settings:
* Fixed image set to H&E and moving image set to 8-bit DAPI.
* Tilesize 1000 um.
  
## Segmentation & Classification
This is done in QuPath using Cellpose or Instanseg and results in classified detections. [See the guide online](https://qupath.readthedocs.io/en/stable/docs/tutorials/cell_classification.html).

## Transformation of objects
The segmented and classified objects can be transfered from CODEX to H&E using the transform.json file in QuPath. Using [script 3](scripts/set_parent.groovy), the detection objects are added as children to the parent object (the annotation). [Script 4](scripts/tps_transform.groovy) performs the inverse transformation (CODEX -> H&E) on the selected objects. The hierarchical structure of the parent and children objects is preserved and after the transformation the objects maintain their original UUIDs. This is important when you want to refer back to objects in the CODEX image. Afterwards patches can be exported using [script 5](scripts/export_patches.groovy).
