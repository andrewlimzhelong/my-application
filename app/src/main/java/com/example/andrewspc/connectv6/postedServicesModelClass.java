package com.example.andrewspc.connectv6;

public class postedServicesModelClass {

    private String Date;
    private String Time;

    private String Category;
    private String Decimal;
    private String Pricing;
    private String Reviews;
    private String portfolioPic1;
    private String userID;
    private String UniqueName;
    private String Details;
    private String Title;
    private String Profile;
    private String HPcontact;
    private String officeNum;
    private String ProfilePicture;
    private String CountryLocation;
    private String StateLocation;
    private String yourCompany;
    private String yourCompanyDesc;
    private String imageUniqueKey;

    public postedServicesModelClass() {
    }

    public postedServicesModelClass(String Decimal, String Pricing, String Reviews, String portfolioPic1,
                                    String userID, String UniqueName, String Details, String Title, String Profile,
                                    String HPcontact, String ProfilePicture, String CountryLocation, String StateLocation,
                                    String yourCompany, String yourCompanyDesc, String officeNum, String imageUniqueKey,
                                    String Category, String Date, String Time) {
        this.Date = Date;
        this.Time = Time;
        this.Category = Category;
        this.HPcontact = HPcontact;
        this.officeNum = officeNum;
        this.Profile = Profile;
        this.userID = userID;
        this.Decimal = Decimal;
        this.Pricing = Pricing;
        this.Reviews = Reviews;
        this.portfolioPic1 = portfolioPic1;
        this.UniqueName = UniqueName;
        this.Details = Details;
        this.Title = Title;
        this.ProfilePicture = ProfilePicture;
        this.CountryLocation = CountryLocation;
        this.StateLocation = StateLocation;
        this.yourCompany = yourCompany;
        this.yourCompanyDesc = yourCompanyDesc;
        this.imageUniqueKey = imageUniqueKey;
    }


    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String Time) {
        this.Time = Time;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String Category) {
        this.Category = Category;
    }

    public String getImageUniqueKey() {
        return imageUniqueKey;
    }

    public void setImageUniqueKey(String imageUniqueKey) {
        this.imageUniqueKey = imageUniqueKey;
    }

    public String getofficeNum() {
        return officeNum;
    }

    public void setofficeNum(String officeNum) {
        this.officeNum = officeNum;
    }

    public String getYourCompany() {
        return yourCompany;
    }

    public void setYourCompany(String yourCompany) {
        this.yourCompany = yourCompany;
    }

    public String getYourCompanyDesc() {
        return yourCompanyDesc;
    }

    public void setYourCompanyDesc(String yourCompanyDesc) {
        this.yourCompanyDesc = yourCompanyDesc;
    }

    public String getCountryLocation() {
        return CountryLocation;
    }

    public void setCountryLocation(String countryLocation) {
        this.CountryLocation = countryLocation;
    }

    public String getStateLocation() {
        return StateLocation;
    }

    public void setStateLocation(String stateLocation) {
        this.StateLocation = stateLocation;
    }

    public String getProfilePicture() {
        return ProfilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        ProfilePicture = profilePicture;
    }

    public String getHPcontact() {
        return HPcontact;
    }

    public void setHPcontact(String hpcontact) {
        HPcontact = hpcontact;
    }

    public String getProfile() {
        return Profile;
    }

    public void setProfile(String profile) {
        Profile = profile;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String profile) {
        Details = profile;
    }

    public String getUniqueName() {
        return UniqueName;
    }

    public void setUniqueName(String uniqueName) {
        UniqueName = uniqueName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDecimal() {
        return Decimal;
    }

    public void setDecimal(String Decimal) {
        this.Decimal = Decimal;
    }

    public String getPricing() {
        return Pricing;
    }

    public void setPricing(String Pricing) {
        this.Pricing = Pricing;
    }

    public String getReviews() {
        return Reviews;
    }

    public void setReviews(String Reviews) {
        this.Reviews = Reviews;
    }

    public String getportfolioPic1() {
        return portfolioPic1;
    }

    public void setportfolioPic1(String portfolioPic1) {
        this.portfolioPic1 = portfolioPic1;
    }
}
