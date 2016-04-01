package com.melvinphilips.moviefreak;

/**
 * Created by melvin on 3/30/16.
 */
public class GridItem {
    private String detailImage;
    private String detailName;
    private String detailReleaseDate;
    private String overView;
    private double detailVoteAverage;
    
    public GridItem(){
        super();
    }

    public String getDetailImage() {
        return detailImage;
    }

    public void setDetailImage(String image) {
        this.detailImage = image;
    }

    public String getDetailName() {
        return detailName;
    }

    public void setDetailName(String name) {
        this.detailName = name;
    }

    public String getDetailReleaseDate() {
        return detailReleaseDate;
    }

    public void setDetailReleaseDate(String detailReleaseDate) {
        this.detailReleaseDate = detailReleaseDate;
    }

    public String getDetailOverView() {
        return overView;
    }

    public void setDetailOverView(String overView) {
        this.overView = overView;
    }

    public double getDetailVoteAverage() {
        return detailVoteAverage;
    }

    public void setDetailVoteAverage(double detailVoteAverage) {
        this.detailVoteAverage = detailVoteAverage;
    }
}
