package com.fenghuang.baselib.utils

import android.Manifest.permission.CALL_PHONE
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.annotation.RequiresPermission
import androidx.core.content.FileProvider
import java.io.File

object IntentUtils {

    /**
     * Return the intent of install app.
     *
     * Target APIs greater than 25 must hold
     * `<uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />`
     *
     * @param filePath  The path of file.
     * @param authority Target APIs greater than 23 must hold the authority of a FileProvider
     * defined in a `<provider>` element in your app's manifest.
     * @return the intent of install app
     */
    fun getInstallAppIntent(filePath: String, authority: String): Intent? {
        return getInstallAppIntent(FileUtils.getFileByPath(filePath), authority)
    }

    /**
     * Return the intent of install app.
     *
     * Target APIs greater than 25 must hold
     * `<uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />`
     *
     * @param file      The file.
     * @param authority Target APIs greater than 23 must hold the authority of a FileProvider
     * defined in a `<provider>` element in your app's manifest.
     * @return the intent of install app
     */
    fun getInstallAppIntent(file: File?, authority: String): Intent? {
        return getInstallAppIntent(file, authority, false)
    }

    /**
     * Return the intent of install app.
     *
     * Target APIs greater than 25 must hold
     * `<uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />`
     *
     * @param file      The file.
     * @param authority Target APIs greater than 23 must hold the authority of a FileProvider
     * defined in a `<provider>` element in your app's manifest.
     * @param isNewTask True to add flag of new task, false otherwise.
     * @return the intent of install app
     */
    fun getInstallAppIntent(file: File?,
                            authority: String,
                            isNewTask: Boolean): Intent? {
        if (file == null) return null
        val intent = Intent(Intent.ACTION_VIEW)
        val data: Uri
        val type = "application/vnd.android.package-archive"
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            data = Uri.fromFile(file)
        } else {
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            data = FileProvider.getUriForFile(AppUtils.getContext(), authority, file)
        }
        intent.setDataAndType(data, type)
        return getIntent(intent, isNewTask)
    }

    /**
     * Return the intent of uninstall app.
     *
     * @param packageName The name of the package.
     * @return the intent of uninstall app
     */
    fun getUninstallAppIntent(packageName: String): Intent {
        return getUninstallAppIntent(packageName, false)
    }

    /**
     * Return the intent of uninstall app.
     *
     * @param packageName The name of the package.
     * @param isNewTask   True to add flag of new task, false otherwise.
     * @return the intent of uninstall app
     */
    fun getUninstallAppIntent(packageName: String, isNewTask: Boolean): Intent {
        val intent = Intent(Intent.ACTION_DELETE)
        intent.data = Uri.parse("package:$packageName")
        return getIntent(intent, isNewTask)
    }

    /**
     * Return the intent of launch app.
     *
     * @param packageName The name of the package.
     * @return the intent of launch app
     */
    fun getLaunchAppIntent(packageName: String): Intent? {
        return getLaunchAppIntent(packageName, false)
    }

    /**
     * Return the intent of launch app.
     *
     * @param packageName The name of the package.
     * @param isNewTask   True to add flag of new task, false otherwise.
     * @return the intent of launch app
     */
    fun getLaunchAppIntent(packageName: String, isNewTask: Boolean): Intent? {
        val intent = AppUtils.getContext().packageManager.getLaunchIntentForPackage(packageName)
                ?: return null
        return getIntent(intent, isNewTask)
    }

    /**
     * Return the intent of launch app details settings.
     *
     * @param packageName The name of the package.
     * @return the intent of launch app details settings
     */
    fun getLaunchAppDetailsSettingsIntent(packageName: String): Intent {
        return getLaunchAppDetailsSettingsIntent(packageName, false)
    }

    /**
     * Return the intent of launch app details settings.
     *
     * @param packageName The name of the package.
     * @param isNewTask   True to add flag of new task, false otherwise.
     * @return the intent of launch app details settings
     */
    fun getLaunchAppDetailsSettingsIntent(packageName: String,
                                          isNewTask: Boolean): Intent {
        val intent = Intent("android.settings.APPLICATION_DETAILS_SETTINGS")
        intent.data = Uri.parse("package:$packageName")
        return getIntent(intent, isNewTask)
    }

    /**
     * Return the intent of share text.
     *
     * @param content The content.
     * @return the intent of share text
     */
    fun getShareTextIntent(content: String): Intent {
        return getShareTextIntent(content, false)
    }

    /**
     * Return the intent of share text.
     *
     * @param content   The content.
     * @param isNewTask True to add flag of new task, false otherwise.
     * @return the intent of share text
     */

    fun getShareTextIntent(content: String, isNewTask: Boolean): Intent {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, content)
        return getIntent(intent, isNewTask)
    }

    /**
     * Return the intent of share image.
     *
     * @param content   The content.
     * @param imagePath The path of image.
     * @return the intent of share image
     */
    fun getShareImageIntent(content: String, imagePath: String): Intent? {
        return getShareImageIntent(content, imagePath, false)
    }

    /**
     * Return the intent of share image.
     *
     * @param content   The content.
     * @param imagePath The path of image.
     * @param isNewTask True to add flag of new task, false otherwise.
     * @return the intent of share image
     */
    fun getShareImageIntent(content: String,
                            imagePath: String?,
                            isNewTask: Boolean): Intent? {
        return if (imagePath == null || imagePath.length == 0) null else getShareImageIntent(content, File(imagePath), isNewTask)
    }

    /**
     * Return the intent of share image.
     *
     * @param content The content.
     * @param image   The file of image.
     * @return the intent of share image
     */
    fun getShareImageIntent(content: String, image: File): Intent? {
        return getShareImageIntent(content, image, false)
    }

    /**
     * Return the intent of share image.
     *
     * @param content   The content.
     * @param image     The file of image.
     * @param isNewTask True to add flag of new task, false otherwise.
     * @return the intent of share image
     */
    fun getShareImageIntent(content: String,
                            image: File?,
                            isNewTask: Boolean): Intent? {
        return if (image != null && image.isFile) null else getShareImageIntent(content, Uri.fromFile(image), isNewTask)
    }

    /**
     * Return the intent of share image.
     *
     * @param content The content.
     * @param uri     The uri of image.
     * @return the intent of share image
     */
    fun getShareImageIntent(content: String, uri: Uri): Intent {
        return getShareImageIntent(content, uri, false)
    }

    /**
     * Return the intent of share image.
     *
     * @param content   The content.
     * @param uri       The uri of image.
     * @param isNewTask True to add flag of new task, false otherwise.
     * @return the intent of share image
     */
    fun getShareImageIntent(content: String,
                            uri: Uri,
                            isNewTask: Boolean): Intent {
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT, content)
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.type = "image/*"
        return getIntent(intent, isNewTask)
    }

    /**
     * Return the intent of component.
     *
     * @param packageName The name of the package.
     * @param className   The name of class.
     * @return the intent of component
     */
    fun getComponentIntent(packageName: String, className: String): Intent {
        return getComponentIntent(packageName, className, null, false)
    }

    /**
     * Return the intent of component.
     *
     * @param packageName The name of the package.
     * @param className   The name of class.
     * @param isNewTask   True to add flag of new task, false otherwise.
     * @return the intent of component
     */
    fun getComponentIntent(packageName: String,
                           className: String,
                           isNewTask: Boolean): Intent {
        return getComponentIntent(packageName, className, null, isNewTask)
    }

    /**
     * Return the intent of component.
     *
     * @param packageName The name of the package.
     * @param className   The name of class.
     * @param bundle      The Bundle of extras to add to this intent.
     * @return the intent of component
     */
    fun getComponentIntent(packageName: String,
                           className: String,
                           bundle: Bundle): Intent {
        return getComponentIntent(packageName, className, bundle, false)
    }

    /**
     * Return the intent of component.
     *
     * @param packageName The name of the package.
     * @param className   The name of class.
     * @param bundle      The Bundle of extras to add to this intent.
     * @param isNewTask   True to add flag of new task, false otherwise.
     * @return the intent of component
     */
    fun getComponentIntent(packageName: String,
                           className: String,
                           bundle: Bundle?,
                           isNewTask: Boolean): Intent {
        val intent = Intent(Intent.ACTION_VIEW)
        if (bundle != null) intent.putExtras(bundle)
        val cn = ComponentName(packageName, className)
        intent.component = cn
        return getIntent(intent, isNewTask)
    }

    /**
     * Return the intent of shutdown.
     *
     * Requires root permission
     * or hold `android:sharedUserId="android.uid.system"`,
     * `<uses-permission android:name="android.permission.SHUTDOWN/>`
     * in manifest.
     *
     * @return the intent of shutdown
     */
    fun getShutdownIntent(): Intent {
        return getShutdownIntent(false)
    }

    /**
     * Return the intent of shutdown.
     *
     * Requires root permission
     * or hold `android:sharedUserId="android.uid.system"`,
     * `<uses-permission android:name="android.permission.SHUTDOWN/>`
     * in manifest.
     *
     * @param isNewTask True to add flag of new task, false otherwise.
     * @return the intent of shutdown
     */
    fun getShutdownIntent(isNewTask: Boolean): Intent {
        val intent = Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN")
        intent.putExtra("android.intent.extra.KEY_CONFIRM", false)
        return getIntent(intent, isNewTask)
    }

    /**
     * Return the intent of dial.
     *
     * @param phoneNumber The phone number.
     * @return the intent of dial
     */
    fun getDialIntent(phoneNumber: String): Intent {
        return getDialIntent(phoneNumber, false)
    }

    /**
     * Return the intent of dial.
     *
     * @param phoneNumber The phone number.
     * @param isNewTask   True to add flag of new task, false otherwise.
     * @return the intent of dial
     */
    fun getDialIntent(phoneNumber: String, isNewTask: Boolean): Intent {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
        return getIntent(intent, isNewTask)
    }

    /**
     * Return the intent of call.
     *
     * Must hold `<uses-permission android:name="android.permission.CALL_PHONE" />`
     *
     * @param phoneNumber The phone number.
     * @return the intent of call
     */
    @RequiresPermission(CALL_PHONE)
    fun getCallIntent(phoneNumber: String): Intent {
        return getCallIntent(phoneNumber, false)
    }

    /**
     * Return the intent of call.
     *
     * Must hold `<uses-permission android:name="android.permission.CALL_PHONE" />`
     *
     * @param phoneNumber The phone number.
     * @param isNewTask   True to add flag of new task, false otherwise.
     * @return the intent of call
     */
    @RequiresPermission(CALL_PHONE)
    fun getCallIntent(phoneNumber: String, isNewTask: Boolean): Intent {
        val intent = Intent("android.intent.action.CALL", Uri.parse("tel:$phoneNumber"))
        return getIntent(intent, isNewTask)
    }

    /**
     * Return the intent of send SMS.
     *
     * @param phoneNumber The phone number.
     * @param content     The content of SMS.
     * @return the intent of send SMS
     */
    fun getSendSmsIntent(phoneNumber: String, content: String): Intent {
        return getSendSmsIntent(phoneNumber, content, false)
    }

    /**
     * Return the intent of send SMS.
     *
     * @param phoneNumber The phone number.
     * @param content     The content of SMS.
     * @param isNewTask   True to add flag of new task, false otherwise.
     * @return the intent of send SMS
     */
    fun getSendSmsIntent(phoneNumber: String,
                         content: String,
                         isNewTask: Boolean): Intent {
        val uri = Uri.parse("smsto:$phoneNumber")
        val intent = Intent(Intent.ACTION_SENDTO, uri)
        intent.putExtra("sms_body", content)
        return getIntent(intent, isNewTask)
    }

    /**
     * Return the intent of capture.
     *
     * @param outUri The uri of output.
     * @return the intent of capture
     */
    fun getCaptureIntent(outUri: Uri): Intent {
        return getCaptureIntent(outUri, false)
    }

    /**
     * Return the intent of capture.
     *
     * @param outUri    The uri of output.
     * @param isNewTask True to add flag of new task, false otherwise.
     * @return the intent of capture
     */
    fun getCaptureIntent(outUri: Uri, isNewTask: Boolean): Intent {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outUri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        return getIntent(intent, isNewTask)
    }

    private fun getIntent(intent: Intent, isNewTask: Boolean): Intent {
        return if (isNewTask) intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) else intent
    }

}