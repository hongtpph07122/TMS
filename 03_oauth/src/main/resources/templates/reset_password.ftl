<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta content="ie=edge" http-equiv="x-ua-compatible">
    <title>Password Confirmation</title>
    <meta content="width=device-width, initial-scale=1" name="viewport">
    <style type="text/css">
        /**
        * Google webfonts. Recommended to include the .woff version for cross-client compatibility.
        */
        @media screen {
            @font-face {
                font-family: 'Source Sans Pro';
                font-style: normal;
                font-weight: 400;
                src: local('Source Sans Pro Regular'), local('SourceSansPro-Regular'), url(https://fonts.gstatic.com/s/sourcesanspro/v10/ODelI1aHBYDBqgeIAH2zlBM0YzuT7MdOe03otPbuUS0.woff) format('woff');
            }
            @font-face {
                font-family: 'Source Sans Pro';
                font-style: normal;
                font-weight: 700;
                src: local('Source Sans Pro Bold'), local('SourceSansPro-Bold'), url(https://fonts.gstatic.com/s/sourcesanspro/v10/toadOcfmlt9b38dHJxOBGFkQc6VGVFSmCnC_l7QZG60.woff) format('woff');
            }
        }

        /**
        * Avoid browser level font resizing.
        * 1. Windows Mobile
        * 2. iOS / OSX
        */
        body,
        table,
        td,
        a {
            -ms-text-size-adjust: 100%;
            /* 1 */
            -webkit-text-size-adjust: 100%;
            /* 2 */
        }

        /**
        * Remove extra space added to tables and cells in Outlook.
        */
        table,
        td {
            mso-table-rspace: 0pt;
            mso-table-lspace: 0pt;
        }

        /**
        * Better fluid images in Internet Explorer.
        */
        img {
            -ms-interpolation-mode: bicubic;
        }

        /**
        * Remove blue links for iOS devices.
        */
        a[x-apple-data-detectors] {
            font-family: inherit !important;
            font-size: inherit !important;
            font-weight: inherit !important;
            line-height: inherit !important;
            color: inherit !important;
            text-decoration: none !important;
        }

        /**
        * Fix centering issues in Android 4.4.
        */
        div[style*="margin: 16px 0;"] {
            margin: 0 !important;
        }

        body {
            width: 100% !important;
            height: 100% !important;
            padding: 0 !important;
            margin: 0 !important;
        }

        /**
        * Collapse table borders to avoid space between cells.
        */
        table {
            border-collapse: collapse !important;
        }

        a {
            color: #1a82e2;
        }

        img {
            height: auto;
            line-height: 100%;
            text-decoration: none;
            border: 0;
            outline: none;
        }
    </style>
    <script>
        function clickOn() {
            var x = document.getElementById('content');
            if (x.style.display == 'none') {
                x.style.display = 'block';
            } else {
                x.style.display = 'none';
            }
        }
    </script>
</head>
<body style="background-color: #e9ecef;">
<!-- start preheader -->
<div class="preheader"
     style="display: none; max-width: 0; max-height: 0; overflow: hidden; font-size: 1px; line-height: 1px; color: #fff; opacity: 0;">
    The TMS system sends email automatically when the password expires.
</div>
<!-- end preheader -->
<!-- start body -->
<table border="0" cellpadding="0" cellspacing="0" width="100%">
    <!-- start logo -->
    <tr>
        <td align="center" bgcolor="#e9ecef">
        </td>
    </tr>
    <!-- end logo -->
    <!-- start hero -->
    <tr>
        <td align="center" bgcolor="#e9ecef">
            <table border="0" cellpadding="0" cellspacing="0" style="max-width: 600px;" width="100%">
                <tr>
                    <td align="left" bgcolor="#ffffff"
                        style="padding: 36px 24px 0; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; border-top: 3px solid #d4dadf;">
                        <h1
                                style="margin: 0; font-size: 32px; font-weight: 700; letter-spacing: -1px; line-height: 48px;">
                            New Password
                        </h1>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <!-- end hero -->
    <!-- start copy block -->
    <tr>
        <td align="center" bgcolor="#e9ecef">
            <table border="0" cellpadding="0" cellspacing="0" style="max-width: 600px;" width="100%">
                <!-- start copy -->
                <tr>
                    <td align="left" bgcolor="#ffffff"
                        style="padding: 24px; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px;">
                        <p style="margin: 0;">Hi ${USER}, </p>
                        <p style="margin: 0;">
                            Your password is expired on <b> ${DATE_EXPIRED} </b>. Of course, to be safe, so we create
                            your new password. Let's use this password to access system.
                        </p>
                        <br>
                        <p style="margin: 0;">
                            New password in the box below:
                        </p>
                    </td>
                </tr>
                <!-- end copy -->
                <!-- start button -->
                <tr>
                    <td align="left" bgcolor="#ffffff">
                        <table border="0" cellpadding="0" cellspacing="0" width="100%">
                            <tr>
                                <td align="center" bgcolor="#ffffff" style="padding: 12px;">
                                    <table border="0" cellpadding="0" cellspacing="0">
                                        <tr>
                                            <td align="center" bgcolor="#1a82e2" style="border-radius: 6px;">
                                                <a style="cursor: pointer; ;display: inline-block; padding: 16px 36px; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 16px; color: #ffffff; text-decoration: none; border-radius: 6px;"
                                                   target="_blank"
                                                   type="submit"> ${NEW_PASSWORD} </a>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                                <div id="content"
                                     style="display:none; text-align:center; font-weight: bold; font-size: 50px">
                                    ${NEW_PASSWORD}
                                </div>
                            </tr>
                        </table>
                    </td>
                </tr>
                <!-- end button -->
                <!-- start copy -->
                <tr>
                    <td align="left" bgcolor="#ffffff"
                        style="padding: 24px; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; border-bottom: 3px solid #d4dadf">
                        <p style="margin: 0;">Cheers,<br> TELE247 Global</p>
                    </td>
                </tr>
                <!-- end copy -->
            </table>
        </td>
    </tr>
    <!-- end copy block -->
    <!-- start footer -->
    <tr>
        <td align="center" bgcolor="#e9ecef" style="padding: 24px;">
            <table border="0" cellpadding="0" cellspacing="0" style="max-width: 600px;" width="100%">
                <!-- start permission -->
                <tr>
                    <td align="center" bgcolor="#e9ecef"
                        style="padding: 12px 24px; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 14px; line-height: 20px; color: #666;">
                        <p style="margin: 0;">This message was sent to you by TMS BOT</p>
                    </td>
                </tr>
                <!-- end permission -->
                <!-- start unsubscribe -->
                <tr>
                    <td align="center" bgcolor="#e9ecef"
                        style="padding: 12px 24px; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 14px; line-height: 20px; color: #666;">
                        <p style="margin: 0;">Address 1: 2nd - 3rd floor, HKL Building, 156 Nguyen Huu Tho, Nha Be
                            District,
                            HCM, Vietnam
                        </p>
                        <p style="margin: 0;">Address 2: 4th floor, Focus Building, 85 Nguyen Ngoc Vu, Cau Giay
                            District,
                            Hanoi, Vietnam
                        </p>
                    </td>
                </tr>
                <!-- end unsubscribe -->
            </table>
        </td>
    </tr>
    <!-- end footer -->
</table>
<!-- end body -->
</body>
</html>
