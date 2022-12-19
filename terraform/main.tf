terraform {
 required_providers {
   aws = {
     source = "hashicorp/aws"
   }
 }
}
 
provider "aws" {
 region = "eu-central-1"
}
 
resource "aws_ami_copy" "weather_test_instance" {
  source_ami_id = "ami-0caef02b518350c8b"
  source_ami_region = "eu-central-1"
  instance_type = "t2.micro"
  tags = {
    Name = "weather_test_instance"
  }
}