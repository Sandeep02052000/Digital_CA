package org.tax.mitra.model;

public class ValidateOtpResponse extends GenericResponse<ValidateOtpResponse.Data> {
    public static class Data {
        private boolean verified;
        private int remainingAttempts;
        public boolean isVerified() {
            return verified;
        }

        public void setVerified(boolean verified) {
            this.verified = verified;
        }

        public int getRemainingAttempts() {
            return remainingAttempts;
        }

        public void setRemainingAttempts(int remainingAttempts) {
            this.remainingAttempts = remainingAttempts;
        }
    }
}