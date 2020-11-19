package softuniBlog.bindingModel;

import com.sun.istack.NotNull;

public class UserBindingModel {

    @NotNull
    private String email;

    @NotNull
    private String fullName;

    @NotNull
    private String password;

    @NotNull
    private String confirmPassword;
    //for img upload
    private byte[] photos;

    public byte[] getPhotos() {
		return photos;
	}

	public void setPhotos(byte[] photos) {
		this.photos = photos;
	}

	public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

	
	
	
}
