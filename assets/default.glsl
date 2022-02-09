#type vertex
#version 410
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec4 aColor;
out vec4 fColor;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;
void main() {
    fColor = aColor;
    gl_Position = (projectionMatrix * viewMatrix * modelMatrix) *   vec4(aPos, 1.0);
    //
}

#type fragment
#version 410
in vec4 fColor;
out vec4 color;
void main() {
    color = fColor;
}