#type vertex
#version 410
layout (location = 0) in vec3 aPos;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;
void main() {
    gl_Position = (projectionMatrix * viewMatrix * modelMatrix) *   vec4(aPos, 1.0);
    //
}

#type fragment
#version 410
out vec4 color;
uniform vec3 lightColor;
void main() {
    color = vec4(lightColor, 1.0);
}