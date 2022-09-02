#include <mutex>
#include <thread>

using namespace std;

int WithMutex() {
    int value = 0;
    std::mutex mut;

    auto increment = [&]() {
        int i = 1000000;
        while (i--) {
            std::lock_guard<std::mutex> guard(mut);
            ++value;
        }
    };

    std::thread t0(increment);
    std::thread t1(increment);

    t0.join();
    t1.join();

    return value;
}

int WithoutProtection() {
    int value = 0;

    auto increment = [&]() {
        int i = 1000000;
        while (i--) ++value;
    };

    std::thread t0(increment);
    std::thread t1(increment);

    t0.join();
    t1.join();

    return value;
}

int main() {
    clock_t now;
    int result;

    now = clock();
    result = WithoutProtection();
    printf("WITHOUT PROTECTION: %d in %.4fs\n", result, double(clock() - now) / CLOCKS_PER_SEC);

    now = clock();
    result = WithMutex();
    printf("WITH MUTEX: %d, %.4fs\n", result, double(clock() - now) / CLOCKS_PER_SEC);

    return 0;
}

