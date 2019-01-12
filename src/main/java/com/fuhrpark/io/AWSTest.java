package com.fuhrpark.io;

import com.amazonaws.services.elasticbeanstalk.AWSElasticBeanstalk;
import com.amazonaws.services.elasticbeanstalk.AWSElasticBeanstalkClient;
import com.amazonaws.services.s3.model.Region;

public class AWSTest {
    public static void main(String[] args) {
        try {
            System.out.println();
            AWSElasticBeanstalkClient.builder().setRegion(Region.EU_Frankfurt.toString());
            AWSElasticBeanstalk client=AWSElasticBeanstalkClient.builder().build();
        } catch (Exception e) {
            System.out.println("Yo, credentials sind ");
        }

    }
}
