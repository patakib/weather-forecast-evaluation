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
 
resource "aws_instance" "weather_test_instance" {
  ami           = "ami-0a6b2839d44d781b2"
  instance_type = "t2.micro"

  tags = {
    Name = "weather_test_instance"
  }
}