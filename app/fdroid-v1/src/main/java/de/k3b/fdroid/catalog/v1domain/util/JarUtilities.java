/*
 * Copyright (c) 2022-2023 by k3b.
 *
 * This file is part of org.fdroid.v1domain the fdroid json catalog-format-v1 parser.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>
 */

package de.k3b.fdroid.catalog.v1domain.util;

import org.apache.commons.codec.binary.Hex2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.CodeSigner;
import java.security.MessageDigest;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Formatter;
import java.util.List;
import java.util.jar.JarEntry;

import de.k3b.fdroid.Global;
import de.k3b.fdroid.catalog.CatalogJarException;
import de.k3b.fdroid.domain.entity.Repo;
import de.k3b.fdroid.domain.util.StringUtil;

/**
 * Utils to verify Signing-Cert-s and their Fingerprints and
 * cecking F-Droid-v1 specific restrictions.
 */
public class JarUtilities {
    private static final Logger LOGGER = LoggerFactory.getLogger(Global.LOG_TAG_UTIL);

    protected static final String ERR_CERTIFICATE_SIGNERS_COUNT = "More than one code certificate signers are not allowed!";
    protected static final String ERR_CERTIFICATE_COUNT = "More than one code signing certificates are not allowed!";
    protected static final String ERR_CERTIFICATE_MISMATCH = "Code signing certificate does not match!";
    protected static final String ERR_CERTIFICATE_FINGERPRINT_MISMATCH = "Supplied code signing certificate fingerprint does not match!";
    protected static final String ERR_CERTIFICATE_KEY_LEN = "Code signing certificate key was shorter than 256 bytes (";
    protected static final String ERR_CERTIFICATE_CREATE_FINGERPRINT = "Unable to create Code signing certificate fingerprint";

    /**
     * Gets Signing Cert from jarEntry.
     * <p>
     * Code inspired by getSigningCertFromJar in
     *
     * @return null if there is no certificate
     * @throws CatalogJarException if cert-info does not confrom to v1-fdroid-security-cert-restrictions.
     * @see <a href="https://git.bubu1.eu/Bubu/fdroidclassic/-/blob/main/app/src/main/java/org/fdroid/fdroid/IndexUpdater.java">Bubu IndexUpdater.java</a> .
     * <p>
     * FDroid's index.jar is signed using a particular format and does not allow lots of
     * signing setups that would be valid for a regular jar.  This code validates those
     * restrictions.
     */
    public static X509Certificate getSigningCertFromJar(Repo repo, JarEntry jarEntry) throws CatalogJarException {
        // @see <a href="https://developer.android.com/reference/java/util/jar/JarEntry#getCodeSigners()">JarEntry#getCodeSigners()</a> api since android-api 1
        // always returns null on android-4.2 and android-4.4 devices, works on android-7.0 and android-10
        //
        // from @see <a href="https://docs.oracle.com/javase/8/docs/api/java/util/jar/JarEntry.html#getCodeSigners--">JarEntry.html#getCodeSigners</a>
        //  This method can only be called once the JarEntry has been completely verified by
        //  reading from the entry input stream until the end of the stream has been reached.
        //
        // @see <a href="https://github.com/k3b/F-Droid-Universal-Catalog/issues/4">fuc#4</a>
        final CodeSigner[] codeSigners = jarEntry.getCodeSigners();

        if (codeSigners == null || codeSigners.length == 0) {
            return null;
        }
        /* we could in theory support more than 1, but as of now we do not */
        if (codeSigners.length > 1) {
            throwError(repo, ERR_CERTIFICATE_SIGNERS_COUNT);
        }
        List<? extends Certificate> certs = codeSigners[0].getSignerCertPath().getCertificates();
        if (certs.size() != 1) {
            throwError(repo, ERR_CERTIFICATE_COUNT);
        }
        return (X509Certificate) certs.get(0);
    }

    /**
     * Inspired by verifySigningCertificate in
     * @see <a href="https://git.bubu1.eu/Bubu/fdroidclassic/-/blob/main/app/src/main/java/org/fdroid/fdroid/IndexV1Updater.java">IndexV1Updater.java</a> .
     * <p>
     * Verify that the signing certificate used to sign index-v1.jar
     * matches the signing stored in the database for this repo.  {@link Repo} and
     * {@code repo.signingCertificate} must be pre-loaded from the database before
     * running this, if this is an existing repo.  If the repo does not exist,
     * this will run the TOFU process.
     * <p>
     * Index V1 works with two copies of the signing certificate:
     * <li>in the downloaded jar</li>
     * <li>stored in the local database</li>
     * <p>
     * A new repo can be added with or without the fingerprint of the signing
     * certificate.  If no fingerprint is supplied, then do a pure TOFU and just
     * store the certificate as valid.  If there is a fingerprint, then first
     * check that the signing certificate in the jar matches that fingerprint.
     * <p>
     * This code is also responsible for adding the {@link Repo} instance to the
     * database for the first time.
     * <p>
     *
     * @param rawCertFromJar the {@link X509Certificate} embedded in the downloaded jar
     */
    public static void verifyAndUpdateSigningCertificate(@NotNull Repo repo, @Nullable Certificate rawCertFromJar) throws CatalogJarException {
        if (repo == null) throw new NullPointerException();

        String certFromJar = null; // assume no cert
        if (rawCertFromJar != null) {
            try {
                certFromJar = Hex2.encodeHexString(rawCertFromJar.getEncoded());
            } catch (CertificateEncodingException e) {
                LOGGER.error("Invalid Cerificate in " + repo.getV1Url() + ": " + rawCertFromJar, e);
                certFromJar = null;
            }
        }

        String certFromDb = StringUtil.emptyAsNull(repo.getJarSigningCertificate());
        if (certFromDb != null && (!certFromDb.equalsIgnoreCase(certFromJar))) {
            LOGGER.error("Cerificate mismatch in " + repo.getV1Url() +
                    "\n\tcertFromDb  : " + certFromDb +
                    "\n\tcertFromJar : " + certFromJar);

            throwError(repo, ERR_CERTIFICATE_MISMATCH);
            return;
        }

        String fingerprintFromDb = StringUtil.emptyAsNull(repo.getJarSigningCertificateFingerprint());
        String fingerprintFromJar = (certFromJar == null)
                ? null
                : StringUtil.emptyAsNull(calcFingerprint(repo, rawCertFromJar));

        if (fingerprintFromDb != null && (!fingerprintFromDb.equalsIgnoreCase(fingerprintFromJar))) {
            LOGGER.error("Fingerprint mismatch in " + repo.getV1Url() +
                    "\n\tfingerprintFromDb  : " + fingerprintFromDb +
                    "\n\tfingerprintFromJar : " + fingerprintFromJar);
            throwError(repo, ERR_CERTIFICATE_FINGERPRINT_MISMATCH);
        }

        // sticky certificate set on first run.
        if (certFromDb == null) repo.setJarSigningCertificate(certFromJar);
        // fingerprint is not set on first run
    }

    private static void throwError(@NotNull Repo repo, String errorMessage) {
        throwError(repo, errorMessage, null);
    }

    private static void throwError(@NotNull Repo repo, String errorMessage, Throwable e) {
        repo.setLastErrorMessage(errorMessage);
        throw new CatalogJarException(repo, errorMessage, e);
    }

    /*
     * Inspired by calcFingerprint in
     * @see <a href="https://git.bubu1.eu/Bubu/fdroidclassic/-/blob/main/app/src/main/java/org/fdroid/fdroid/Utils.java">Utils.java</a> .
     */
    public static String calcFingerprint(Repo repo, Certificate cert) {
        if (cert == null) {
            return null;
        }
        try {
            return calcFingerprint(repo, cert.getEncoded());
        } catch (CertificateEncodingException e) {
            LOGGER.error(ERR_CERTIFICATE_CREATE_FINGERPRINT + " for " + cert, e);
            return null;
        }
    }

    /*
     * Inspired by calcFingerprint in
     * @see <a href="https://git.bubu1.eu/Bubu/fdroidclassic/-/blob/main/app/src/main/java/org/fdroid/fdroid/Utils.java">src/main/java/org/fdroid/fdroid/Utils.java</a> .
     */
    public static String calcFingerprint(Repo repo, byte[] key) {
        if (key == null) {
            return null;
        }
        if (key.length < 256) {
            throwError(repo, ERR_CERTIFICATE_KEY_LEN + key.length + "), cannot be valid!");
        }
        String ret = null;
        try {
            // keytool -list -v gives you the SHA-256 fingerprint
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(key);
            byte[] fingerprint = digest.digest();
            Formatter formatter = new Formatter(new StringBuilder());
            for (byte aFingerprint : fingerprint) {
                formatter.format("%02X", aFingerprint);
            }
            ret = formatter.toString();
            formatter.close();
        } catch (Throwable e) {
            throwError(repo, ERR_CERTIFICATE_CREATE_FINGERPRINT, e);
        }
        return ret;
    }


}
