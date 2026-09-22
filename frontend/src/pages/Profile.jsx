import { useState } from "react";

function Profile() {
  const storedUser = JSON.parse(
    localStorage.getItem("user")
  );

  const [email, setEmail] = useState(
    storedUser?.email || ""
  );

  const [currentPassword, setCurrentPassword] =
    useState("");

  const [newPassword, setNewPassword] =
    useState("");

  const [confirmPassword, setConfirmPassword] =
    useState("");

  const [emailMessage, setEmailMessage] =
    useState("");

  const [emailError, setEmailError] =
    useState("");

  const [passwordMessage, setPasswordMessage] =
    useState("");

  const [passwordError, setPasswordError] =
    useState("");

  const [emailLoading, setEmailLoading] =
    useState(false);

  const [passwordLoading, setPasswordLoading] =
    useState(false);

  const handleUpdateEmail = async (event) => {
    event.preventDefault();

    setEmailMessage("");
    setEmailError("");

    if (!email.trim()) {
      setEmailError("Email is required.");
      return;
    }

    try {
      setEmailLoading(true);

      const token = localStorage.getItem("token");

      if (!token) {
        throw new Error("Please log in first.");
      }

      if (!storedUser?.userID) {
        throw new Error(
          "User information is not available."
        );
      }

      const response = await fetch(
        `http://localhost:8080/api/users/${storedUser.userID}/email`,
        {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify({
            email: email.trim(),
          }),
        }
      );

      if (!response.ok) {
        if (response.status === 401) {
          throw new Error(
            "Your login has expired. Please log in again."
          );
        }

        if (response.status === 403) {
          throw new Error(
            "You are not authorized to update this email."
          );
        }

        if (response.status === 409) {
          throw new Error(
            "That email address is already in use."
          );
        }

        const errorText = await response.text();

        throw new Error(
          errorText || "Failed to update email."
        );
      }

      const updatedUser = await response.json();

      const updatedStoredUser = {
        ...storedUser,
        email: updatedUser.email,
      };

      localStorage.setItem(
        "user",
        JSON.stringify(updatedStoredUser)
      );

      setEmail(updatedUser.email);

      setEmailMessage(
        "Email updated successfully."
      );
    } catch (error) {
      setEmailError(error.message);
    } finally {
      setEmailLoading(false);
    }
  };

  const handleChangePassword = async (event) => {
    event.preventDefault();

    setPasswordMessage("");
    setPasswordError("");

    if (!currentPassword) {
      setPasswordError(
        "Current password is required."
      );
      return;
    }

    if (!newPassword) {
      setPasswordError(
        "New password is required."
      );
      return;
    }

    if (newPassword !== confirmPassword) {
      setPasswordError(
        "New passwords do not match."
      );
      return;
    }

    try {
      setPasswordLoading(true);

      const token = localStorage.getItem("token");

      if (!token) {
        throw new Error("Please log in first.");
      }

      if (!storedUser?.userID) {
        throw new Error(
          "User information is not available."
        );
      }

      const response = await fetch(
        `http://localhost:8080/api/users/${storedUser.userID}/password`,
        {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify({
            currentPassword,
            newPassword,
          }),
        }
      );

      if (!response.ok) {
        if (response.status === 401) {
          throw new Error(
            "Current password is incorrect."
          );
        }

        if (response.status === 403) {
          throw new Error(
            "You are not authorized to change this password."
          );
        }

        if (response.status === 400) {
          const errorText = await response.text();

          throw new Error(
            errorText ||
              "Please check your password information."
          );
        }

        throw new Error(
          "Failed to change password."
        );
      }

      setCurrentPassword("");
      setNewPassword("");
      setConfirmPassword("");

      setPasswordMessage(
        "Password changed successfully."
      );
    } catch (error) {
      setPasswordError(error.message);
    } finally {
      setPasswordLoading(false);
    }
  };

  return (
    <main>
      <h2>My Profile</h2>

      <p>
        View and update your BankApp account information.
      </p>

      <section>
        <h3>Profile Information</h3>

        <p>
          <strong>Username:</strong>{" "}
          {storedUser?.username || "Not available"}
        </p>

        <p>
          <strong>User ID:</strong>{" "}
          {storedUser?.userID || "Not available"}
        </p>

        <p>
          <strong>Role:</strong>{" "}
          {storedUser?.role || "Not available"}
        </p>
      </section>

      <br />

      <section>
        <h3>Update Email</h3>

        <form onSubmit={handleUpdateEmail}>
          <div>
            <label htmlFor="profileEmail">
              Email
            </label>

            <br />

            <input
              id="profileEmail"
              type="email"
              value={email}
              onChange={(event) =>
                setEmail(event.target.value)
              }
              required
            />
          </div>

          <br />

          <button
            type="submit"
            disabled={emailLoading}
          >
            {emailLoading
              ? "Updating..."
              : "Update Email"}
          </button>
        </form>

        {emailMessage && (
          <p>{emailMessage}</p>
        )}

        {emailError && (
          <p>{emailError}</p>
        )}
      </section>

      <br />

      <section>
        <h3>Change Password</h3>

        <form onSubmit={handleChangePassword}>
          <div>
            <label htmlFor="currentPassword">
              Current Password
            </label>

            <br />

            <input
              id="currentPassword"
              type="password"
              value={currentPassword}
              onChange={(event) =>
                setCurrentPassword(
                  event.target.value
                )
              }
              required
            />
          </div>

          <br />

          <div>
            <label htmlFor="newPassword">
              New Password
            </label>

            <br />

            <input
              id="newPassword"
              type="password"
              value={newPassword}
              onChange={(event) =>
                setNewPassword(
                  event.target.value
                )
              }
              required
            />
          </div>

          <br />

          <div>
            <label htmlFor="confirmNewPassword">
              Confirm New Password
            </label>

            <br />

            <input
              id="confirmNewPassword"
              type="password"
              value={confirmPassword}
              onChange={(event) =>
                setConfirmPassword(
                  event.target.value
                )
              }
              required
            />
          </div>

          <br />

          <button
            type="submit"
            disabled={passwordLoading}
          >
            {passwordLoading
              ? "Changing..."
              : "Change Password"}
          </button>
        </form>

        {passwordMessage && (
          <p>{passwordMessage}</p>
        )}

        {passwordError && (
          <p>{passwordError}</p>
        )}
      </section>
    </main>
  );
}

export default Profile;