//package gauravdahale.gtech.akoladirectory
//fun pickimage() {
//    ImagePicker.with(this)
//            .setFolderMode(true)
//            .setFolderTitle("Album")
//            .setRootDirectoryName(Config.ROOT_DIR_DCIM)
//            .setDirectoryName("Image Picker")
//            .setMultipleMode(true)
//            .setShowNumberIndicator(true)
//            .setMaxSize(10)
//            .setLimitMessage("You can select up to 10 images")
//            .setSelectedImages(images)
//            .setRequestCode(100)
//            .start();
//}
//
//override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//    // The last parameter value of shouldHandleResult() is the value we pass to setRequestCode().
//    // If we do not call setRequestCode(), we can ignore the last parameter.
//    if (ImagePicker.shouldHandleResult(requestCode, resultCode, data, 100)) {
//        val images: ArrayList<Image> = ImagePicker.getImages(data)
//        // Do stuff with image's path or id. For example:
//        for (image in images) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                Glide.with(context)
//                        .load(image.uri)
//                        .into(imageView)
//            } else {
//                Glide.with(context)
//                        .load(image.path)
//                        .into(imageView)
//            }
//        }
//    }
//    super.onActivityResult(requestCode, resultCode, data)   // This line is REQUIRED in fragment mode
//}