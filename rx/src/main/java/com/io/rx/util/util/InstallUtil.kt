package com.myku.util.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File
import java.util.*
import java.util.regex.Pattern

object InstallUtil {
    /**
     * 启动相机
     *@param packageProvider 相机Provider路径 “包名.fileprovider”
     */
    fun startCamera(activity: Activity, packageProvider: String, file: File, requestCode: Int) {
        file.let { it ->
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                val imgUri = FileProvider.getUriForFile(activity, packageProvider, it)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri)
            } else {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(it))
            }
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
            intent.resolveActivity(activity.packageManager)?.let {
                activity.startActivityForResult(intent, requestCode)
            }
        }
    }


    //调用相册
    fun pickCamera(activity: Activity, requestCode: Int) {
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            val intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            intent.putExtra("scale", true)
            intent.putExtra("scaleUpIfNeeded", true)
            activity.startActivityForResult(intent, requestCode)
        } else {

        }

    }


    /**
     * 生成一个jpeg文件
     */
    fun generatePath(): File {
        val path = Environment.getExternalStorageDirectory().absolutePath + "/avatar/"
        val dir = File(Environment.getExternalStorageDirectory().absolutePath, "avatar")
        if (!dir.exists())
            dir.mkdir()
        val relativePath = UUID.randomUUID().toString() + "." + Bitmap.CompressFormat.JPEG
        val mPathRow = path + relativePath
        return File(mPathRow)
    }

    /**
     * 安装
     *@param packageProvider 相机Provider路径 “包名.fileprovider”
     */
    fun install(context: Context, file: File, packageProvider: String) {
        //7.0以上通过FileProvider
        if (Build.VERSION.SDK_INT >= 24) {
            val uri = FileProvider.getUriForFile(context, packageProvider, file)
            val intent = Intent(Intent.ACTION_VIEW).setDataAndType(
                uri,
                "application/vnd.android.package-archive"
            )
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            context.startActivity(intent)
        } else {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.setDataAndType(
                Uri.fromFile(file),
                "application/vnd.android.package-archive"
            )
            context.startActivity(intent)
        }
    }

    fun getKey(map: HashMap<String, String>, value: String): String? {
        var key: String? = null
        //Map,HashMap并没有实现Iteratable接口.不能用于增强for循环.
        for (getKey in map.keys) {
            if (map[getKey].equals(value)) {
                key = getKey
            }
        }
        return key
        //这个key肯定是最后一个满足该条件的key.
    }

    // 过滤特殊字符
    fun StringFilter(str: String): String {
        // 只允许字母和数字
//        val regEx = "[^a-zA-Z0-9]";
        // 清除掉所有特殊字符
//        val regEx = "[`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]"
        val regEx = "[\u4E00-\u9FA5]"
        val p = Pattern.compile(regEx)
        val m = p.matcher(str)
        return m.replaceAll("").trim()
    }

}