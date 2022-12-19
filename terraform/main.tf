terraform {
 required_providers {
   aws = {
     source = "hashicorp/aws"
   }
 }
}
 
provider "aws" {
 region = "eu-central-1b"
}
 
resource "aws_instance" "test_instance" {
 ami           = "ami-830c94e3"
 instance_type = "t2.nano"
 tags = {
   Name = "test_instance"
 }
}