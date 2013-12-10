#include <iostream>

void change(int & aux) {
    aux = 4;
}

int main() {
    int a = 0;

    change(a);

    std::cout << a << '\n';
}

