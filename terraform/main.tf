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
  ami           = "ami-09c5ba4f838d8684a"
  instance_type = "t2.micro"

  tags = {
    Name = "weather_test_instance"
  }
}